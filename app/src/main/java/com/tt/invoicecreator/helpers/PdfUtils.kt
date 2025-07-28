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
import com.tt.invoicecreator.data.room.Invoice
import com.tt.invoicecreator.ui.theme.CustomColorsPalette
import java.io.File
import java.text.DecimalFormat

class PdfUtils {
    companion object{
        private const val PAGE_WIDTH = 840
        private const val PAGE_HEIGHT = 1188
        private const val LEFT_MARGIN = 50f
        private const val RIGHT_MARGIN = PAGE_WIDTH- LEFT_MARGIN
        private const val MARGIN_BOTTOM = PAGE_HEIGHT-50f
        private const val DESCRIPTION_RIGHT = 300f
        private const val QUANTITY_RIGHT = 400f
        private const val PRICE_RIGHT = 560f
        private const val DISCOUNT_RIGHT = 660f
        private const val MARGIN_TOP = 50f
        private const val TEXT_SMALL = 15f
        private const val TEXT_BIG = 30f
        private const val SEPARATOR_LINE_Y = 140f
        private const val HEAD_TOP = 300f
        private const val TABLE_HEIGHT = 60f

        fun generatePDF(
            context:Context,
            invoice: Invoice
        ){
            /*
            todo change color of the table and invoice word
             */


            val pdfDocument = PdfDocument()
            val paint = Paint()
            val decimalFormat = DecimalFormat("#.00")
            val pageInfo: PdfDocument.PageInfo? = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT,1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            drawUser(context, canvas, paint)

            drawInvoiceWord(context, canvas, paint)

            drawSeparatorLine(context, canvas, paint)

            drawBillToSection(context, canvas, paint, invoice)

            drawInvoiceNumberAndDate(canvas, paint, invoice)

            drawTableHead(context, canvas, paint)

            drawItem(canvas, paint, invoice, decimalFormat)

            drawTotal(context, canvas, paint, invoice, decimalFormat)

            drawPaymentOptions(context, canvas, paint)

            drawSignature(context, canvas, paint)

            pdfDocument.finishPage(page)


            val filename = "invoice ${InvoiceNumber.getStringNumber(invoiceNumber = invoice.invoiceNumber, time = invoice.time)}.pdf"
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

        private fun drawSignature(context: Context,canvas: Canvas,paint: Paint) {

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
                val ratio:Float = width.toFloat()/height.toFloat()
                val signatureHeight = 120f
                val signatureWidth = signatureHeight*ratio

                val destRect = Rect(
                    (RIGHT_MARGIN-signatureWidth-50f).toInt(),
                    (MARGIN_BOTTOM-signatureHeight).toInt(),
                    (RIGHT_MARGIN-50f).toInt(),
                    MARGIN_BOTTOM.toInt()
                )

                canvas.drawBitmap(signature,null,destRect,paint)
            }
        }

        private fun drawPaymentOptions(context: Context,canvas: Canvas,paint: Paint) {

            paint.textAlign = Paint.Align.LEFT
            canvas.drawText(
                "Payment options",
                LEFT_MARGIN+10f,
                HEAD_TOP+ TABLE_HEIGHT*3,
                paint
            )

            val list = SharedPreferences.readPaymentMethod(context)?.split("\n")

            paint.style = Paint.Style.FILL
            if(!list.isNullOrEmpty()){
                var a = 0f
                for(item in list){
                    canvas.drawText(
                        item,
                        LEFT_MARGIN+10f,
                        HEAD_TOP+ TABLE_HEIGHT*3+ TEXT_BIG+a,
                        paint
                    )
                    a += 20f
                }
            }

        }

        private fun drawTotal(context: Context,canvas: Canvas,paint: Paint,invoice: Invoice,decimalFormat: DecimalFormat) {

            paint.color = context.getColor(R.color.orange_light)
            paint.style = Paint.Style.FILL_AND_STROKE
            canvas.drawRect(
                PRICE_RIGHT,
                HEAD_TOP+ TABLE_HEIGHT*2,
                RIGHT_MARGIN,
                HEAD_TOP+ TABLE_HEIGHT*3,
                paint
            )

            paint.color = context.getColor(R.color.black)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                "Total",
                DISCOUNT_RIGHT-10f,
                HEAD_TOP+ TABLE_HEIGHT*2.6f,
                paint
            )

            canvas.drawText(
                "£"+decimalFormat.format(InvoiceValueCalculator.calculate(invoice)),
                RIGHT_MARGIN-10f,
                HEAD_TOP+ TABLE_HEIGHT*2.6f,
                paint
            )
        }

        private fun drawItem(canvas: Canvas,paint: Paint,invoice: Invoice,decimalFormat:DecimalFormat) {
            paint.textAlign = Paint.Align.LEFT
            paint.textSize = TEXT_SMALL
            paint.style = Paint.Style.FILL
            if(invoice.comment.isNotEmpty()){
                canvas.drawText(
                    invoice.item.itemName,
                    LEFT_MARGIN+10f,
                    HEAD_TOP+(TABLE_HEIGHT*1.4f),
                    paint
                )
                canvas.drawText(
                    invoice.comment,
                    LEFT_MARGIN+10f,
                    HEAD_TOP+(TABLE_HEIGHT*1.8f),
                    paint
                )
            }else{
                canvas.drawText(
                    invoice.item.itemName,
                    LEFT_MARGIN+10f,
                    HEAD_TOP+ (TABLE_HEIGHT*1.66f),
                    paint
                )
            }

            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(
                invoice.itemCount.toString(),
                Math.average(DESCRIPTION_RIGHT, QUANTITY_RIGHT),
                HEAD_TOP+ (TABLE_HEIGHT*1.6f),
                paint
            )

            canvas.drawText(
                "£"+decimalFormat.format(invoice.item.itemValue),
                Math.average(QUANTITY_RIGHT, PRICE_RIGHT),
                HEAD_TOP+ (TABLE_HEIGHT*1.6f),
                paint
            )
            canvas.drawText(
                if(invoice.itemDiscount!=0.0){
                    "£"+decimalFormat.format(invoice.itemDiscount)
                }else{
                    "----"
                },

                Math.average(PRICE_RIGHT,DISCOUNT_RIGHT),
                HEAD_TOP+ (TABLE_HEIGHT*1.6f),
                paint
            )
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                "£"+decimalFormat.format(InvoiceValueCalculator.calculate(invoice)),
//                InvoiceValueCalculator.calculate(invoice).toString(),
                RIGHT_MARGIN-10f,
                HEAD_TOP+ (TABLE_HEIGHT*1.6f),
                paint
            )

        }

        private fun drawTableHead(context: Context,canvas: Canvas,paint: Paint) {

            paint.color = context.getColor(R.color.orange_light)
            canvas.drawRect(
                LEFT_MARGIN,
                HEAD_TOP,
                RIGHT_MARGIN,
                HEAD_TOP+ TABLE_HEIGHT,
                paint
            )
            paint.color = context.getColor(R.color.orange_light)
            paint.style = Paint.Style.STROKE
            canvas.drawRect(
                LEFT_MARGIN,
                HEAD_TOP+ TABLE_HEIGHT,
                DESCRIPTION_RIGHT,
                HEAD_TOP+(TABLE_HEIGHT*2),
                paint
            )
            canvas.drawRect(
                DESCRIPTION_RIGHT,
                HEAD_TOP+ TABLE_HEIGHT,
                QUANTITY_RIGHT,
                HEAD_TOP+(TABLE_HEIGHT*2),
                paint
            )
            canvas.drawRect(
                QUANTITY_RIGHT,
                HEAD_TOP+ TABLE_HEIGHT,
                PRICE_RIGHT,
                HEAD_TOP+(TABLE_HEIGHT*2),
                paint
            )
            canvas.drawRect(
                PRICE_RIGHT,
                HEAD_TOP+ TABLE_HEIGHT,
                DISCOUNT_RIGHT,
                HEAD_TOP+(TABLE_HEIGHT*2),
                paint
            )
            canvas.drawRect(
                DISCOUNT_RIGHT,
                HEAD_TOP+ TABLE_HEIGHT,
                RIGHT_MARGIN,
                HEAD_TOP+(TABLE_HEIGHT*2),
                paint
            )


            paint.textSize = TEXT_SMALL
            paint.color = context.getColor(R.color.black)
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText(
                "Description",
                LEFT_MARGIN+10f,
                HEAD_TOP+ TABLE_HEIGHT*0.6f,
                paint
            )
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(
                "QTY",
                Math.average(DESCRIPTION_RIGHT, QUANTITY_RIGHT),
                HEAD_TOP+ TABLE_HEIGHT*0.6f,
                paint
            )

            canvas.drawText(
                "Price",
                Math.average(QUANTITY_RIGHT, PRICE_RIGHT),
                HEAD_TOP+ TABLE_HEIGHT*0.6f,
                paint
            )
            canvas.drawText(
                "Discount",
                Math.average(PRICE_RIGHT,DISCOUNT_RIGHT),
                HEAD_TOP+ TABLE_HEIGHT*0.6f,
                paint
            )
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                "Amount",
                RIGHT_MARGIN-10f,
                HEAD_TOP+ TABLE_HEIGHT*0.6f,
                paint
            )

        }

        private fun drawInvoiceNumberAndDate(canvas: Canvas,paint: Paint,invoice: Invoice) {
            paint.style = Paint.Style.FILL_AND_STROKE
            canvas.drawText(
                "Invoice #",
                PRICE_RIGHT,
                SEPARATOR_LINE_Y + (TEXT_SMALL*1.2f),
                paint
            )
            paint.style = Paint.Style.FILL
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                InvoiceNumber.getStringNumber(invoiceNumber = invoice.invoiceNumber, time = invoice.time),
                RIGHT_MARGIN,
                SEPARATOR_LINE_Y + (TEXT_SMALL*1.2f),
                paint
            )
            canvas.drawText(
                DateAndTime.convertLongToDate(time = invoice.time),
                RIGHT_MARGIN,
                SEPARATOR_LINE_Y + (TEXT_SMALL*2.5f),
                paint
            )

        }

        private fun drawBillToSection(context: Context,canvas: Canvas,paint: Paint, invoice: Invoice) {
            paint.textSize = TEXT_SMALL
            paint.color = context.getColor(R.color.black)
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText(
                "Bill To:",
                LEFT_MARGIN,
                SEPARATOR_LINE_Y + (TEXT_SMALL*1.2f),
                paint
            )

            paint.style = Paint.Style.FILL
            canvas.drawText(
                invoice.client.clientName,
                LEFT_MARGIN,
                SEPARATOR_LINE_Y + (TEXT_SMALL*3f),
                paint
            )
            canvas.drawText(
                invoice.client.clientAddress1,
                LEFT_MARGIN,
                SEPARATOR_LINE_Y + (TEXT_SMALL*4.5f),
                paint
            )
            canvas.drawText(
                invoice.client.clientAddress2,
                LEFT_MARGIN,
                SEPARATOR_LINE_Y + (TEXT_SMALL*6f),
                paint
            )
            canvas.drawText(
                invoice.client.clientCity,
                LEFT_MARGIN,
                SEPARATOR_LINE_Y + (TEXT_SMALL*7.5f),
                paint
            )
        }

        private fun drawSeparatorLine(context: Context, canvas: Canvas, paint: Paint) {
            paint.color = context.getColor(R.color.orange)
            canvas.drawLine(LEFT_MARGIN, SEPARATOR_LINE_Y, RIGHT_MARGIN, SEPARATOR_LINE_Y,paint)
        }

        private fun drawInvoiceWord(context: Context,canvas: Canvas,paint: Paint) {
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

        private fun drawUser(context: Context,canvas: Canvas,paint: Paint) {
            paint.textSize = TEXT_SMALL
            paint.color = context.getColor(R.color.black)
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText(
                "Tomasz Turkiewicz",
                LEFT_MARGIN,
                MARGIN_TOP,
                paint
            )
            canvas.drawText(
                "Flat 3, 10 Morley Street",
                LEFT_MARGIN,
                MARGIN_TOP+(TEXT_SMALL*1.5f),
                paint
            )
            canvas.drawText(
                "BN2 9RA",
                LEFT_MARGIN,
                MARGIN_TOP+(TEXT_SMALL*3f),
                paint
            )
            canvas.drawText(
                "BRIGHTON",
                LEFT_MARGIN,
                MARGIN_TOP+(TEXT_SMALL*4.5f),
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