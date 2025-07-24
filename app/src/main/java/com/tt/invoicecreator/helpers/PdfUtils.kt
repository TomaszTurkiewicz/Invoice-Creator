package com.tt.invoicecreator.helpers

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.tt.invoicecreator.R
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.data.room.Invoice
import java.io.File
import java.text.DecimalFormat
import kotlin.math.roundToLong

class PdfUtils {
    companion object{

        fun generatePDF(
            context:Context,
            invoice: Invoice
        ){

            val pdfDocument = PdfDocument()
            val paint = Paint()
            val decimalFormat = DecimalFormat("#.00")

            val pageInfo: PdfDocument.PageInfo? = PdfDocument.PageInfo.Builder(420,594,1).create()
            val page = pdfDocument.startPage(pageInfo)
            val left = 10f
            val right = 410f
            val descRight = 150f
            val qtyRight = 200f
            val priceRight = 280f
            val discRight = 330f
            val canvas = page.canvas
            paint.textSize = 10f
            paint.color = context.getColor(R.color.black)
            paint.textAlign = Paint.Align.LEFT

            /** USER **/
            canvas.drawText(
                "Tomasz Turkiewicz",
                10f,
                10f,
                paint
            )
            canvas.drawText(
                "Flat 3, 10 Morley Street",
                10f,
                25f,
                paint
            )
            canvas.drawText(
                "BN2 9RA",
                10f,
                40f,
                paint
            )
            canvas.drawText(
                "BRIGHTON",
                10f,
                55f,
                paint
            )

            /** INVOICE **/
            paint.textSize = 20f
            paint.color = context.getColor(R.color.purple_500)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                "INVOICE",
                410f,
                20f,
                paint
            )

            /** separator line **/
            paint.color = context.getColor(R.color.teal_700)
            canvas.drawLine(10f,70f,410f,70f,paint)

            /** BILL TO **/
            paint.textSize = 15f
            paint.color = context.getColor(R.color.black)
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText(
                "Bill To:",
                10f,
                85f,
                paint
            )
            paint.textSize = 10f
            canvas.drawText(
                invoice.client.clientName,
                10f,
                100f,
                paint
            )
            canvas.drawText(
                invoice.client.clientAddress1,
                10f,
                115f,
                paint
            )
            canvas.drawText(
                invoice.client.clientAddress2,
                10f,
                130f,
                paint
            )
            canvas.drawText(
                invoice.client.clientCity,
                10f,
                145f,
                paint
            )

            /** invoice number and date **/
            canvas.drawText(
                "Invoice #",
                300f,
                85f,
                paint
            )

            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                InvoiceNumber.getStringNumber(invoiceNumber = invoice.invoiceNumber, time = invoice.time),
                410f,
                85f,
                paint
            )
            canvas.drawText(
                DateAndTime.convertLongToDate(time = invoice.time),
                410f,
                100f,
                paint
            )


            /** HEAD plus table **/

            paint.color = context.getColor(R.color.teal_200)
            canvas.drawRect(
                left,
                150f,
                right,
                180f,
                paint
            )
            paint.color = context.getColor(R.color.purple_200)
            paint.style = Paint.Style.STROKE
            canvas.drawRect(
                left,
                180f,
                descRight,
                210f,
                paint
            )
            canvas.drawRect(
                descRight,
                180f,
                qtyRight,
                210f,
                paint
            )
            canvas.drawRect(
                qtyRight,
                180f,
                priceRight,
                210f,
                paint
            )
            canvas.drawRect(
                priceRight,
                180f,
                discRight,
                210f,
                paint
            )
            canvas.drawRect(
                discRight,
                180f,
                right,
                210f,
                paint
            )


            paint.textSize = 12f
            paint.color = context.getColor(R.color.black)
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText(
                "Description",
                left+10f,
                170f,
                paint
            )
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(
                "QTY",
                Math.average(descRight,qtyRight),
                170f,
                paint
            )

            canvas.drawText(
                "Price",
                Math.average(qtyRight,priceRight),
                170f,
                paint
            )
            canvas.drawText(
                "Discount",
                Math.average(priceRight,discRight),
                170f,
                paint
            )
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                "Amount",
                right-10f,
                170f,
                paint
            )

            /** item **/
            paint.textAlign = Paint.Align.LEFT
            paint.textSize = 10f
            paint.style = Paint.Style.FILL
            if(invoice.comment.isNotEmpty()){
                canvas.drawText(
                    invoice.item.itemName,
                    left+10f,
                    193f,
                    paint
                )
                canvas.drawText(
                    invoice.comment,
                    left+10f,
                    207f,
                    paint
                )
            }else{
                canvas.drawText(
                    invoice.item.itemName,
                    left+10f,
                    200f,
                    paint
                )
            }

            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(
                invoice.itemCount.toString(),
                Math.average(descRight,qtyRight),
                200f,
                paint
            )

            canvas.drawText(
                "£"+decimalFormat.format(invoice.item.itemValue),
                Math.average(qtyRight,priceRight),
                200f,
                paint
            )
            canvas.drawText(
                if(invoice.itemDiscount!=0.0){
                    "£"+decimalFormat.format(invoice.itemDiscount)
                }else{
                    "----"
                },

                Math.average(priceRight,discRight),
                200f,
                paint
            )
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                "£"+decimalFormat.format(InvoiceValueCalculator.calculate(invoice)),
//                InvoiceValueCalculator.calculate(invoice).toString(),
                right-10f,
                200f,
                paint
            )


            /** total **/

            paint.color = context.getColor(R.color.teal_200)
            paint.style = Paint.Style.FILL_AND_STROKE
            canvas.drawRect(
                priceRight,
                210f,
                right,
                240f,
                paint
            )

            paint.color = context.getColor(R.color.black)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                "Total",
                discRight-10f,
                230f,
                paint
            )

            canvas.drawText(
                "£"+decimalFormat.format(InvoiceValueCalculator.calculate(invoice)),
                right-10f,
                230f,
                paint
            )

            /** payment options**/
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText(
                "Payment options",
                left+10f,
                260f,
                paint
            )

            val list = SharedPreferences.readPaymentMethod(context)?.split("\n")

            if(!list.isNullOrEmpty()){
                var a = 0f
                for(item in list){
                    canvas.drawText(
                        item,
                        left+10f,
                        280f+a,
                        paint
                    )
                    a += 20f
                }
            }



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