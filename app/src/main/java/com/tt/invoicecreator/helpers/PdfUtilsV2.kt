package com.tt.invoicecreator.helpers

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.tt.invoicecreator.R
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.data.SignatureFile
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import java.io.File

class PdfUtilsV2 {
    companion object{
        private const val PAGE_WIDTH = 840
        private const val PAGE_HEIGHT = 1188
        private const val LEFT_MARGIN = 50f
        private const val RIGHT_MARGIN = PAGE_WIDTH- LEFT_MARGIN
        private const val MARGIN_BOTTOM = PAGE_HEIGHT-50f
        private const val DESCRIPTION_RIGHT = 300f
        private const val DESCRIPTION_RIGHT_VAT = 240f
        private const val QUANTITY_RIGHT = 400f
        private const val QUANTITY_RIGHT_VAT = 330f
        private const val PRICE_RIGHT = 560f
        private const val PRICE_RIGHT_VAT = 450f
        private const val DISCOUNT_RIGHT = 660f
        private const val DISCOUNT_RIGHT_VAT = 520f

        private const val AMOUNT_RIGHT_VAT = 610f
        private const val VAT_VAT = 670f
        private const val MARGIN_TOP = 50f
        private const val TEXT_SMALL = 15f
        private const val TEXT_BIG = 30f
        private const val SEPARATOR_LINE_Y = 140f
        private const val HEAD_TOP = 300f
        private const val TABLE_HEIGHT = 60f
        private var i = 1

        fun generateInvoicePdfV2(
            context: Context,
            invoiceV2: InvoiceV2,
            items:List<InvoiceItemV2>
        )
        {
            val user = SharedPreferences.readUserDetails(context)

            val pdfDocument = PdfDocument()
            val paint = Paint()
//            val decimalFormat = DecimalFormat("#.00")
            val pageInfo: PdfDocument.PageInfo? = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT,1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            val vatBoolean = InvoiceValueCalculator.checkIfVATList(items)


            drawUserV2(context, user, canvas, paint)

            drawInvoiceWordV2(context, canvas, paint)

            drawSeparatorLineV2(context, canvas, paint)

            drawBillToSectionV2(context, canvas, paint, invoiceV2)

            drawInvoiceNumberAndDateV2(canvas, paint, invoiceV2)

            if(vatBoolean){
                drawTableHeadVATV2(context, canvas, paint)

                drawItemVATV2(context, canvas, paint, items)

                drawTotalVATV2(context, canvas, paint, items)
            }else{
                drawTableHeadV2(context, canvas, paint)

                drawItemV2(context, canvas, paint, items)

                drawTotalV2(context, canvas, paint, items)
            }



            drawPaymentOptionsV2(context, canvas, paint)

            drawSignatureV2(context, canvas, paint)

            pdfDocument.finishPage(page)

            val filename = "invoice ${InvoiceNumber.getStringNumber(invoiceNumber = invoiceV2.invoiceNumber, time = invoiceV2.time)}.pdf"
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                val uri = createDownloadUri(context,filename) ?: return
                try{
                    context.contentResolver.openOutputStream(uri,"w")?.use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                        Toast.makeText(context, "$filename is generated...", Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e:Exception){
                    e.printStackTrace()
                }
            }
            else{
                val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(downloadsFolder,filename)
                try {
                    file.outputStream().use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                        Toast.makeText(context, "$filename is generated...", Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e:Exception){
                    e.printStackTrace()
                }
            }
            pdfDocument.close()
        }

        fun generateInfoPdfV2(
            context: Context,
            invoiceV2: InvoiceV2,
            items: List<InvoiceItemV2>,
            paidList: List<PaidV2>?
        )
        {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT,1).create()
            val myPage = pdfDocument.startPage(pageInfo)
            val canvas = myPage.canvas


            // --- PAINTS ---
            val titlePaint = Paint().apply {
                textAlign = Paint.Align.CENTER
                textSize = 36f
                isFakeBoldText = true
                color = context.getColor(R.color.black)
            }

            val headerPaint = Paint().apply {
                textSize = 20f
                isFakeBoldText = true
                color = context.getColor(R.color.dark_gray)
            }

            val bodyPaint = Paint().apply {
                textSize = 18f
                color = context.getColor(R.color.black)
            }

            val accentPaint = Paint().apply {
                textSize = 18f
                color = context.getColor(R.color.orange_light)
                isFakeBoldText = true
            }

            val linePaint = Paint().apply {
                color = context.getColor(R.color.light_gray)
                strokeWidth = 2f
            }

            var y = MARGIN_TOP + 40f

            // --- HEADER ---

            canvas.drawText("INVOICE SUMMARY", (PAGE_WIDTH/2).toFloat(), y ,titlePaint)
            y+=60f

            // Invoice # and Date
            val dateString = DateAndTime.convertLongToDate(invoiceV2.time)
            val invNumber = InvoiceNumber.getStringNumber(invoiceV2.invoiceNumber,invoiceV2.time)
            canvas.drawText("Invoice #: $invNumber", LEFT_MARGIN, y, headerPaint)
            bodyPaint.textAlign = Paint.Align.RIGHT
            canvas.drawText("Date: $dateString", RIGHT_MARGIN, y, bodyPaint)
            y+=30f

            if(invoiceV2.dueDate != null){
                val dueDateString = DateAndTime.convertLongToDate(invoiceV2.dueDate!!)
                canvas.drawText("Due: $dueDateString", RIGHT_MARGIN, y, bodyPaint)
                y+=30f
            }
            bodyPaint.textAlign = Paint.Align.LEFT

            // divider line
            y += 10f
            canvas.drawLine(LEFT_MARGIN, y, RIGHT_MARGIN, y, linePaint)
            y += 40f

            // --- CLIENT INFO ---
            canvas.drawText("Bill To:", LEFT_MARGIN, y, accentPaint)
            y += 30f
            canvas.drawText(invoiceV2.client.clientName, LEFT_MARGIN, y, headerPaint)
            y += 25f
            canvas.drawText(invoiceV2.client.clientAddress1, LEFT_MARGIN, y, bodyPaint)
            y += 25f
            if (invoiceV2.client.clientAddress2.isNotEmpty()) {
                canvas.drawText(invoiceV2.client.clientAddress2, LEFT_MARGIN, y, bodyPaint)
                y += 25f
            }
            canvas.drawText(invoiceV2.client.clientCity, LEFT_MARGIN, y, bodyPaint)
            y += 60f

            // FINANCIAL SUMMARY
            canvas.drawText("FINANCIAL BREAKDOWN", LEFT_MARGIN, y, accentPaint)
            y += 20f
            canvas.drawLine(LEFT_MARGIN, y, RIGHT_MARGIN, y, linePaint)
            y += 40f

            val currency = items.firstOrNull()?.itemV2?.itemCurrency ?: Currency.GBP
            val formatter = CurrencyFormatter()

            // Calculations
            val totalNet = InvoiceValueCalculator.calculateNettoV2(items)
            val totalVAT = InvoiceValueCalculator.calculateVATV2(items)
            val totalGross = InvoiceValueCalculator.calculateV2(items)
            val totalPaid = InvoiceValueCalculator.calculatePaid(paidList)
            val remaining = totalGross - totalPaid

            val labelX = LEFT_MARGIN + 20f
            val valueX = RIGHT_MARGIN - 20f

            canvas.drawText("Total Net Value", labelX, y, bodyPaint)
            bodyPaint.textAlign = Paint.Align.RIGHT
            canvas.drawText(formatter.format(totalNet, currency), valueX, y, bodyPaint)
            bodyPaint.textAlign = Paint.Align.LEFT
            y += 35f

            // VAT
            canvas.drawText("Total VAT:", labelX, y, bodyPaint)
            bodyPaint.textAlign = Paint.Align.RIGHT
            canvas.drawText(formatter.format(totalVAT, currency), valueX, y, bodyPaint)
            bodyPaint.textAlign = Paint.Align.LEFT
            y += 35f

            // Line
            canvas.drawLine(labelX, y, RIGHT_MARGIN, y, linePaint)
            y += 35f

            // Gross (Bold)
            canvas.drawText("TOTAL GROSS:", labelX, y, headerPaint)
            headerPaint.textAlign = Paint.Align.RIGHT
            canvas.drawText(formatter.format(totalGross, currency), valueX, y, headerPaint)
            headerPaint.textAlign = Paint.Align.LEFT
            y += 60f

            // --- PAYMENT STATUS

            // Payment History Box Background
            val boxTop = y
            val boxColor = Paint().apply {
                color = context.getColor(R.color.light_gray_background)
            } // Ensure this color exists or use Color.LTGRAY
            // We estimate box height based on items. Base height 150 + 30 per item
            val listSize = paidList?.size ?: 0
            val boxHeight = 120f + (listSize * 30f)

            canvas.drawRect(LEFT_MARGIN, boxTop, RIGHT_MARGIN, boxTop + boxHeight, boxColor)

            y += 40f
            canvas.drawText("PAYMENT HISTORY:", LEFT_MARGIN + 20f, y, headerPaint)
            y += 40f

            if (paidList.isNullOrEmpty()) {
                canvas.drawText("No payments recorded.", LEFT_MARGIN + 20f, y, bodyPaint)
            } else {
                for (payment in paidList) {
                    val pDate = DateAndTime.convertLongToDate(payment.time)
                    val pAmount = formatter.format(payment.amountPaid, currency)
                    canvas.drawText("- Paid on $pDate:   $pAmount", LEFT_MARGIN + 20f, y, bodyPaint)
                    y += 30f
                }
            }

            // Status Result (Green or Red)
            y = boxTop + boxHeight + 50f
            val statusPaint = Paint().apply {
                textSize = 24f
                isFakeBoldText = true
                color =
                    if (remaining <= 0.01) context.getColor(R.color.green) else context.getColor(R.color.red) // Ensure colors exist
            }

            val statusText = if (remaining <= 0.01) "STATUS: PAID IN FULL" else "STATUS: UNPAID (${
                formatter.format(
                    remaining,
                    currency
                )
            } DUE)"
            canvas.drawText(statusText, LEFT_MARGIN, y, statusPaint)

            // --- FOOTER ---

            val footerPaint = Paint().apply {
                textSize = 14f
                color = context.getColor(R.color.dark_gray)
                textAlign = Paint.Align.CENTER
            }

            canvas.drawText(
                "Generated by Invoice Creator App - Summary Info",
                (PAGE_WIDTH / 2).toFloat(),
                MARGIN_BOTTOM,
                footerPaint
            )

            // Finish Page
            pdfDocument.finishPage(myPage)

            // Write File
            val fileName = "Info_${invoiceV2.invoiceNumber}_${invoiceV2.time}.pdf"

            // Save logic similar to your existing generateInvoicePdfV2
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val uri = createDownloadUri(context, fileName)
                if (uri != null) {
                    try {
                        context.contentResolver.openOutputStream(uri, "w")?.use { outputStream ->
                            pdfDocument.writeTo(outputStream)
                            // Note: Toast should be handled on Main thread by the caller
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
                val downloadsFolder =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(downloadsFolder, fileName)
                try {
                    file.outputStream().use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            pdfDocument.close()

        }

        private fun drawTotalVATV2(
            context: Context,
            canvas: Canvas,
            paint: Paint,
            items: List<InvoiceItemV2>
        ) {

            paint.color = context.getColor(R.color.orange_light)
            paint.style = Paint.Style.FILL_AND_STROKE
            canvas.drawRect(
                PRICE_RIGHT_VAT,
                HEAD_TOP + TABLE_HEIGHT * i,
                RIGHT_MARGIN,
                HEAD_TOP + TABLE_HEIGHT *(1+ i),
                paint
            )

            paint.color = context.getColor(R.color.black)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                "Total",
                DISCOUNT_RIGHT_VAT -10f,
                HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                paint
            )

            /**AMOUNT**/
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(
 //               "£" + decimalFormat.format(InvoiceValueCalculator.calculateNettoV2(items)),
                CurrencyFormatter().format(InvoiceValueCalculator.calculateNettoV2(items), items[0].itemV2.itemCurrency),
                Math.average(
                    DISCOUNT_RIGHT_VAT,
                    AMOUNT_RIGHT_VAT
                ),
                HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                paint
            )
            /**VAT**/
                canvas.drawText(
//                    "£" + decimalFormat.format(InvoiceValueCalculator.calculateVATV2(items)),
                    CurrencyFormatter().format(InvoiceValueCalculator.calculateVATV2(items), items[0].itemV2.itemCurrency),
                    Math.average(
                        AMOUNT_RIGHT_VAT,
                        VAT_VAT
                    ),
                    HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                    paint
                )

            /** TOTAL **/
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
//                "£" + decimalFormat.format(InvoiceValueCalculator.calculateV2(items)),
                CurrencyFormatter().format(InvoiceValueCalculator.calculateV2(items), items[0].itemV2.itemCurrency),
                RIGHT_MARGIN - 10f,
                HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                paint
            )



        }

        private fun drawItemVATV2(
            context: Context,
            canvas: Canvas,
            paint: Paint,
            items: List<InvoiceItemV2>
        ) {

            paint.textAlign = Paint.Align.LEFT
            paint.textSize = TEXT_SMALL

            for (itemV2 in items) {

                paint.color = context.getColor(R.color.orange_light)
                paint.style = Paint.Style.STROKE
                /** DESCRIPTION**/
                canvas.drawRect(
                    LEFT_MARGIN,
                    HEAD_TOP + TABLE_HEIGHT*i,
                    DESCRIPTION_RIGHT_VAT,
                    HEAD_TOP + (TABLE_HEIGHT*i + TABLE_HEIGHT),
                    paint
                )
                /** QUANTITY**/
                canvas.drawRect(
                    DESCRIPTION_RIGHT_VAT,
                    HEAD_TOP + TABLE_HEIGHT*i,
                    QUANTITY_RIGHT_VAT,
                    HEAD_TOP + (TABLE_HEIGHT*i + TABLE_HEIGHT),
                    paint
                )
                /** PRICE**/
                canvas.drawRect(
                    QUANTITY_RIGHT_VAT,
                    HEAD_TOP + TABLE_HEIGHT*i,
                    PRICE_RIGHT_VAT,
                    HEAD_TOP + (TABLE_HEIGHT*i + TABLE_HEIGHT),
                    paint
                )
                /** DISCOUNT**/
                canvas.drawRect(
                    PRICE_RIGHT_VAT,
                    HEAD_TOP + TABLE_HEIGHT*i,
                    DISCOUNT_RIGHT_VAT,
                    HEAD_TOP + (TABLE_HEIGHT*i + TABLE_HEIGHT),
                    paint
                )
                /** AMOUNT **/
                canvas.drawRect(
                    DISCOUNT_RIGHT_VAT,
                    HEAD_TOP + TABLE_HEIGHT*i,
                    AMOUNT_RIGHT_VAT,
                    HEAD_TOP + (TABLE_HEIGHT*i + TABLE_HEIGHT),
                    paint
                )

                /** VAT **/
                canvas.drawRect(
                    AMOUNT_RIGHT_VAT,
                    HEAD_TOP + TABLE_HEIGHT*i,
                    VAT_VAT,
                    HEAD_TOP + (TABLE_HEIGHT*i + TABLE_HEIGHT),
                    paint
                )
                /** TOTAL **/
                canvas.drawRect(
                    VAT_VAT,
                    HEAD_TOP + TABLE_HEIGHT*i,
                    RIGHT_MARGIN,
                    HEAD_TOP + (TABLE_HEIGHT*i + TABLE_HEIGHT),
                    paint
                )


                paint.color = context.getColor(R.color.black)
                paint.style = Paint.Style.FILL
                paint.textAlign = Paint.Align.LEFT
                /** DESCRIPTION**/
                if (itemV2.comment.isNotEmpty()) {
                    canvas.drawText(
                        itemV2.itemV2.itemName,
                        LEFT_MARGIN + 10f,
                        HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.4f),
                        paint
                    )
                    canvas.drawText(
                        itemV2.comment,
                        LEFT_MARGIN + 10f,
                        HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.8f),
                        paint
                    )
                } else {
                    canvas.drawText(
                        itemV2.itemV2.itemName,
                        LEFT_MARGIN + 10f,
                        HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                        paint
                    )
                }

                paint.textAlign = Paint.Align.CENTER
                /** QUANTITY**/
                canvas.drawText(
                    itemV2.itemCount.toString(),
                    Math.average(
                        DESCRIPTION_RIGHT_VAT,
                        QUANTITY_RIGHT_VAT
                    ),
                    HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                    paint
                )

                /** PRICE**/
                canvas.drawText(
//                    "£" + decimalFormat.format(itemV2.itemV2.itemValue),
                    CurrencyFormatter().format(itemV2.itemV2.itemValue,itemV2.itemV2.itemCurrency),
                    Math.average(
                        QUANTITY_RIGHT_VAT,
                        PRICE_RIGHT_VAT
                    ),
                    HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                    paint
                )
                /** DISCOUNT **/
                canvas.drawText(
                    if (itemV2.itemDiscount != 0.0) {
                        CurrencyFormatter().format(itemV2.itemDiscount,itemV2.itemV2.itemCurrency)
                    } else {
                        "----"
                    },

                    Math.average(
                        PRICE_RIGHT_VAT,
                        DISCOUNT_RIGHT_VAT
                    ),
                    HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                    paint
                )
                /**AMOUNT**/
                canvas.drawText(
//                    "£" + decimalFormat.format(InvoiceValueCalculator.calculateV2oneNettoItem(itemV2)),
                    CurrencyFormatter().format(InvoiceValueCalculator.calculateV2oneNettoItem(itemV2),itemV2.itemV2.itemCurrency),
                    Math.average(
                        DISCOUNT_RIGHT_VAT,
                        AMOUNT_RIGHT_VAT
                    ),
                    HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                    paint
                )
                /**VAT**/
                if(InvoiceValueCalculator.checkIfVATOneItem(itemV2)){
                    canvas.drawText(
//                        "£" + decimalFormat.format(InvoiceValueCalculator.calculateV2oneVATItem(itemV2)),
                        CurrencyFormatter().format(InvoiceValueCalculator.calculateV2oneVATItem(itemV2),itemV2.itemV2.itemCurrency),
                        Math.average(
                            AMOUNT_RIGHT_VAT,
                            VAT_VAT
                        ),
                        HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                        paint
                    )
                }

                /** TOTAL **/
                paint.textAlign = Paint.Align.RIGHT
                canvas.drawText(
//                    "£" + decimalFormat.format(InvoiceValueCalculator.calculateV2oneTotalItem(itemV2)),
                    CurrencyFormatter().format(InvoiceValueCalculator.calculateV2oneTotalItem(itemV2),itemV2.itemV2.itemCurrency),
                    RIGHT_MARGIN - 10f,
                    HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                    paint
                )
                i += 1
            }


        }

        private fun drawTableHeadVATV2(
            context: Context,
            canvas: Canvas,
            paint: Paint
        ) {
            paint.color = context.getColor(R.color.orange_light)
            canvas.drawRect(
                LEFT_MARGIN,
                HEAD_TOP,
                RIGHT_MARGIN,
                HEAD_TOP + TABLE_HEIGHT,
                paint
            )

            paint.textSize = TEXT_SMALL
            paint.color = context.getColor(R.color.black)
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText(
                "Description",
                LEFT_MARGIN +10f,
                HEAD_TOP + TABLE_HEIGHT *0.6f,
                paint
            )
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(
                "QTY",
                Math.average(
                    DESCRIPTION_RIGHT_VAT,
                    QUANTITY_RIGHT_VAT
                ),
                HEAD_TOP + TABLE_HEIGHT *0.6f,
                paint
            )

            canvas.drawText(
                "Price",
                Math.average(
                    QUANTITY_RIGHT_VAT,
                    PRICE_RIGHT_VAT
                ),
                HEAD_TOP + TABLE_HEIGHT *0.6f,
                paint
            )
            canvas.drawText(
                "Discount",
                Math.average(
                    PRICE_RIGHT_VAT,
                    DISCOUNT_RIGHT_VAT
                ),
                HEAD_TOP + TABLE_HEIGHT *0.6f,
                paint
            )
            canvas.drawText(
                "Amount",
                Math.average(
                    DISCOUNT_RIGHT_VAT,
                    AMOUNT_RIGHT_VAT
                ),
                HEAD_TOP + TABLE_HEIGHT *0.6f,
                paint
            )
            canvas.drawText(
                "VAT",
                Math.average(
                    AMOUNT_RIGHT_VAT,
                    VAT_VAT
                ),
                HEAD_TOP + TABLE_HEIGHT *0.6f,
                paint
            )
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                "Total",
                RIGHT_MARGIN -10f,
                HEAD_TOP + TABLE_HEIGHT *0.6f,
                paint
            )

        }

        private fun drawSignatureV2(context: Context, canvas: Canvas, paint: Paint) {
            val pic = SignatureFile.getFilePath(context).toUri().path
            val file = pic?.let {
                File(it)
            }
            if(file!!.exists()){
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888

                val signature = BitmapFactory.decodeFile(file.path,options)
                val width = signature.width
                val height = signature.height
                val ratio: Float = width.toFloat() / height.toFloat()
                val signatureHeight = 110f
                val signatureWidth = signatureHeight*ratio

                val destRect = Rect(
                    (RIGHT_MARGIN -signatureWidth-50f).toInt(),
                    (MARGIN_BOTTOM -signatureHeight).toInt(),
                    (RIGHT_MARGIN -50f).toInt(),
                    MARGIN_BOTTOM.toInt()
                )

                canvas.drawBitmap(signature,null,destRect,paint)
            }

        }

        private fun drawPaymentOptionsV2(context: Context, canvas: Canvas, paint: Paint) {
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText(
                "Payment options",
                LEFT_MARGIN +10f,
                HEAD_TOP + TABLE_HEIGHT * (1+ i),
                paint
            )

            val list = SharedPreferences.readPaymentMethod(context)?.split("\n")

            paint.style = Paint.Style.FILL
            if(!list.isNullOrEmpty()){
                var a = 0f
                for(item in list){
                    canvas.drawText(
                        item,
                        LEFT_MARGIN +10f,
                        HEAD_TOP + TABLE_HEIGHT *(1+i)+ TEXT_BIG +a,
                        paint
                    )
                    a += 20f
                }
            }
        }

        private fun drawTotalV2(
            context: Context,
            canvas: Canvas,
            paint: Paint,
            items: List<InvoiceItemV2>
        ) {
            paint.color = context.getColor(R.color.orange_light)
            paint.style = Paint.Style.FILL_AND_STROKE
            canvas.drawRect(
                PRICE_RIGHT,
                HEAD_TOP + TABLE_HEIGHT * i,
                RIGHT_MARGIN,
                HEAD_TOP + TABLE_HEIGHT *(1+ i),
                paint
            )

            paint.color = context.getColor(R.color.black)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                "Total",
                DISCOUNT_RIGHT -10f,
                HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                paint
            )

            canvas.drawText(
 //               "£"+decimalFormat.format(InvoiceValueCalculator.calculateV2(items)),
                CurrencyFormatter().format(InvoiceValueCalculator.calculateV2(items), items[0].itemV2.itemCurrency),
                RIGHT_MARGIN -10f,
                HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                paint
            )

        }

        private fun drawItemV2(
            context: Context,
            canvas: Canvas,
            paint: Paint,
            items: List<InvoiceItemV2>
        ) {
            paint.textAlign = Paint.Align.LEFT
            paint.textSize = TEXT_SMALL

            for (itemV2 in items) {

                paint.color = context.getColor(R.color.orange_light)
                paint.style = Paint.Style.STROKE
                canvas.drawRect(
                    LEFT_MARGIN,
                    HEAD_TOP + TABLE_HEIGHT*i,
                    DESCRIPTION_RIGHT,
                 HEAD_TOP + (TABLE_HEIGHT*i + TABLE_HEIGHT),
                    paint
                )
                canvas.drawRect(
                    DESCRIPTION_RIGHT,
                 HEAD_TOP + TABLE_HEIGHT*i,
                    QUANTITY_RIGHT,
                    HEAD_TOP + (TABLE_HEIGHT*i + TABLE_HEIGHT),
                    paint
                )
                canvas.drawRect(
                    QUANTITY_RIGHT,
                    HEAD_TOP + TABLE_HEIGHT*i,
                    PRICE_RIGHT,
                    HEAD_TOP + (TABLE_HEIGHT*i + TABLE_HEIGHT),
                    paint
                )
                canvas.drawRect(
                    PRICE_RIGHT,
                    HEAD_TOP + TABLE_HEIGHT*i,
                    DISCOUNT_RIGHT,
                    HEAD_TOP + (TABLE_HEIGHT*i + TABLE_HEIGHT),
                    paint
                )
                canvas.drawRect(
                    DISCOUNT_RIGHT,
                    HEAD_TOP + TABLE_HEIGHT*i,
                    RIGHT_MARGIN,
                    HEAD_TOP + (TABLE_HEIGHT*i + TABLE_HEIGHT),
                    paint
                )


                paint.color = context.getColor(R.color.black)
                paint.style = Paint.Style.FILL
                paint.textAlign = Paint.Align.LEFT
                if (itemV2.comment.isNotEmpty()) {
                    canvas.drawText(
                        itemV2.itemV2.itemName,
                        LEFT_MARGIN + 10f,
                        HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.4f),
                        paint
                    )
                    canvas.drawText(
                        itemV2.comment,
                        LEFT_MARGIN + 10f,
                        HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.8f),
                        paint
                    )
                } else {
                    canvas.drawText(
                        itemV2.itemV2.itemName,
                        LEFT_MARGIN + 10f,
                        HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                        paint
                    )
                }

                paint.textAlign = Paint.Align.CENTER
                canvas.drawText(
                    itemV2.itemCount.toString(),
                    Math.average(
                        DESCRIPTION_RIGHT,
                        QUANTITY_RIGHT
                    ),
                    HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                    paint
                )

                canvas.drawText(
//                    "£" + decimalFormat.format(itemV2.itemV2.itemValue),
                    CurrencyFormatter().format(itemV2.itemV2.itemValue,itemV2.itemV2.itemCurrency),
                    Math.average(
                        QUANTITY_RIGHT,
                        PRICE_RIGHT
                    ),
                    HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                    paint
                )
                canvas.drawText(
                    if (itemV2.itemDiscount != 0.0) {
//                        "£" + decimalFormat.format(itemV2.itemDiscount)
                        CurrencyFormatter().format(itemV2.itemDiscount,itemV2.itemV2.itemCurrency)
                    } else {
                        "----"
                    },

                    Math.average(
                        PRICE_RIGHT,
                        DISCOUNT_RIGHT
                    ),
                    HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                    paint
                )
                paint.textAlign = Paint.Align.RIGHT
                canvas.drawText(
//                    "£" + decimalFormat.format(InvoiceValueCalculator.calculateV2oneNettoItem(itemV2)),
                    CurrencyFormatter().format(InvoiceValueCalculator.calculateV2oneNettoItem(itemV2),itemV2.itemV2.itemCurrency),
                    RIGHT_MARGIN - 10f,
                    HEAD_TOP + TABLE_HEIGHT*(i-1) + (TABLE_HEIGHT * 1.6f),
                    paint
                )
                i += 1
            }
        }

        private fun drawTableHeadV2(context: Context, canvas: Canvas, paint: Paint) {
            paint.color = context.getColor(R.color.orange_light)
            canvas.drawRect(
                LEFT_MARGIN,
                HEAD_TOP,
                RIGHT_MARGIN,
                HEAD_TOP + TABLE_HEIGHT,
                paint
            )

            paint.textSize = TEXT_SMALL
            paint.color = context.getColor(R.color.black)
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText(
                "Description",
                LEFT_MARGIN +10f,
                HEAD_TOP + TABLE_HEIGHT *0.6f,
                paint
            )
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(
                "QTY",
                Math.average(
                    DESCRIPTION_RIGHT,
                    QUANTITY_RIGHT
                ),
                HEAD_TOP + TABLE_HEIGHT *0.6f,
                paint
            )

            canvas.drawText(
                "Price",
                Math.average(
                    QUANTITY_RIGHT,
                    PRICE_RIGHT
                ),
                HEAD_TOP + TABLE_HEIGHT *0.6f,
                paint
            )
            canvas.drawText(
                "Discount",
                Math.average(
                    PRICE_RIGHT,
                    DISCOUNT_RIGHT
                ),
                HEAD_TOP + TABLE_HEIGHT *0.6f,
                paint
            )
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                "Amount",
                RIGHT_MARGIN -10f,
                HEAD_TOP + TABLE_HEIGHT *0.6f,
                paint
            )
        }

        private fun drawInvoiceNumberAndDateV2(canvas: Canvas, paint: Paint, invoiceV2: InvoiceV2) {
            paint.style = Paint.Style.FILL_AND_STROKE
            canvas.drawText(
                "Invoice #",
                PRICE_RIGHT,
                SEPARATOR_LINE_Y + (TEXT_SMALL *1.2f),
                paint
            )
            paint.style = Paint.Style.FILL
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                InvoiceNumber.getStringNumber(invoiceNumber = invoiceV2.invoiceNumber, time = invoiceV2.time),
                RIGHT_MARGIN,
                SEPARATOR_LINE_Y + (TEXT_SMALL *1.2f),
                paint
            )
            canvas.drawText(
                DateAndTime.convertLongToDate(time = invoiceV2.time),
                RIGHT_MARGIN,
                SEPARATOR_LINE_Y + (TEXT_SMALL *2.5f),
                paint
            )
            if(invoiceV2.dueDate!=null){
                canvas.drawText(
                    "Due: "+DateAndTime.convertLongToDate(time = invoiceV2.dueDate!!),
                    RIGHT_MARGIN,
                    SEPARATOR_LINE_Y + (TEXT_SMALL *3.8f),
                    paint
                )
            }
        }

        private fun drawBillToSectionV2(
            context: Context,
            canvas: Canvas,
            paint: Paint,
            invoiceV2: InvoiceV2
        ) {
            paint.textSize = TEXT_SMALL
            paint.color = context.getColor(R.color.black)
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText(
                "Bill To:",
                LEFT_MARGIN,
                SEPARATOR_LINE_Y + (TEXT_SMALL *1.2f),
                paint
            )

            paint.style = Paint.Style.FILL
            canvas.drawText(
                invoiceV2.client.clientName,
                LEFT_MARGIN,
                SEPARATOR_LINE_Y + (TEXT_SMALL *3f),
                paint
            )
            canvas.drawText(
                invoiceV2.client.clientAddress1,
                LEFT_MARGIN,
                SEPARATOR_LINE_Y + (TEXT_SMALL *4.5f),
                paint
            )
            canvas.drawText(
                invoiceV2.client.clientAddress2,
                LEFT_MARGIN,
                SEPARATOR_LINE_Y + (TEXT_SMALL *6f),
                paint
            )
            canvas.drawText(
                invoiceV2.client.clientCity,
                LEFT_MARGIN,
                SEPARATOR_LINE_Y + (TEXT_SMALL *7.5f),
                paint
            )
        }

        private fun drawSeparatorLineV2(context: Context, canvas: Canvas, paint: Paint) {
            paint.color = context.getColor(R.color.orange)
            canvas.drawLine(LEFT_MARGIN, SEPARATOR_LINE_Y, RIGHT_MARGIN, SEPARATOR_LINE_Y,paint)
        }

        private fun drawInvoiceWordV2(context: Context, canvas: Canvas, paint: Paint) {
            paint.textSize = TEXT_BIG
            paint.color = context.getColor(R.color.orange)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                "INVOICE",
                RIGHT_MARGIN,
                MARGIN_TOP,
                paint
            )
        }

        private fun drawUserV2(context: Context, user:User, canvas: Canvas, paint: Paint) {
            paint.textSize = TEXT_SMALL
            paint.color = context.getColor(R.color.black)
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText(
                user.userName ?: "",
                LEFT_MARGIN,
                MARGIN_TOP,
                paint
            )
            canvas.drawText(
                user.userAddressLine1 ?: "",
                LEFT_MARGIN,
                MARGIN_TOP +(TEXT_SMALL *1.5f),
                paint
            )
            canvas.drawText(
                user.userAddressLine2 ?: "",
                LEFT_MARGIN,
                MARGIN_TOP +(TEXT_SMALL *3f),
                paint
            )
            canvas.drawText(
                user.userCity ?: "",
                LEFT_MARGIN,
                MARGIN_TOP +(TEXT_SMALL *4.5f),
                paint
            )
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        private fun createDownloadUri(context: Context, filename: String): Uri? {
            val downloadsCollection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val newFileValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, filename)
            }
            return context.contentResolver.insert(downloadsCollection,newFileValues)
        }
    }
}