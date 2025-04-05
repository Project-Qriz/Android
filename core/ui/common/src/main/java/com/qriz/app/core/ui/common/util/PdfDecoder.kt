package com.qriz.app.core.ui.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import coil3.Extras
import coil3.ImageLoader
import coil3.asImage
import coil3.decode.DecodeResult
import coil3.decode.Decoder
import coil3.fetch.SourceFetchResult
import coil3.request.Options
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/***
 * TODO: Refactoring 필요
 */
class PdfPageDecoder(
    private val context: Context,
    private val data: PdfPage
) : Decoder {
    companion object {
        const val NUMBER_PDF_PAGES = 0
        const val SPACE_BETWEEN_PAGES = 1
    }

    override suspend fun decode(): DecodeResult {
        val bitmap = withContext(Dispatchers.IO) {
            renderPagesToBitmap(
                data.file,
                context.resources.displayMetrics.density,
                data.numberPages,
                data.spaceBetweenPages
            )
        }
        val drawable = BitmapDrawable(context.resources, bitmap).asImage()
        return DecodeResult(image = drawable, isSampled = false)
    }

    class Factory : Decoder.Factory {
        override fun create(
            result: SourceFetchResult,
            options: Options,
            imageLoader: ImageLoader
        ): Decoder? {
            return if (isApplicable(result)) {
                val numberPages = (options.extras[Extras.Key(NUMBER_PDF_PAGES)]) ?: 1
                val spaceBetweenPages = options.extras[Extras.Key(SPACE_BETWEEN_PAGES)] ?: 0
                PdfPageDecoder(
                    context = options.context,
                    data = PdfPage(
                        file = result.source.file().toFile(),
                        numberPages = numberPages,
                        spaceBetweenPages = spaceBetweenPages
                    )
                )
            } else {
                null
            }
        }

        private fun isApplicable(result: SourceFetchResult): Boolean {
            return result.mimeType?.contains("pdf") == true
        }
    }

    private fun renderPagesToBitmap(
        file: File,
        density: Float,
        pagesNumber: Int,
        spaceBetweenPages: Int
    ): Bitmap {
        val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(fileDescriptor)

        val spaceBetweenPagesPx = (spaceBetweenPages * density).toInt()
        val pagesSize = pagesNumber.coerceIn(1, pdfRenderer.pageCount)
        val (maxWidth, totalHeight) = calculateTotalHeightAndWidth(
            pdfRenderer,
            2f,
            pagesSize,
            spaceBetweenPagesPx
        )

        val combinedBitmap = Bitmap.createBitmap(maxWidth, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(combinedBitmap)

        var currentY = 0
        for (i in 0 until pdfRenderer.pageCount) {
            pdfRenderer.openPage(i).use { page ->
                val pageBitmap = createPageBitmap(page, 2f)
                page.render(pageBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                // Draw the page bitmap on the combined bitmap
                canvas.drawBitmap(pageBitmap, 0f, currentY.toFloat(), null)
                currentY += pageBitmap.height + spaceBetweenPagesPx

                pageBitmap.recycle() // Recycle the page bitmap to free memory
            }
        }

        pdfRenderer.close()
        fileDescriptor.close()

        return combinedBitmap
    }

    private fun calculateTotalHeightAndWidth(
        pdfRenderer: PdfRenderer,
        density: Float,
        pagesSize: Int,
        spaceBetweenPagesPx: Int
    ): Pair<Int, Int> {
        var totalHeight = 0
        var maxWidth = 0
        for (i in 0 until pagesSize) {
            pdfRenderer.openPage(i).use { page ->
                Log.d("PdfPageDecoder", "pageWidth: ${page.width}, pageHeight: ${page.height}")
                totalHeight += (page.height * density).toInt() + spaceBetweenPagesPx
                maxWidth = maxOf(maxWidth, (page.width * density).toInt())
            }
        }
        totalHeight -= spaceBetweenPagesPx
        return Pair(maxWidth, totalHeight)
    }

    private fun createPageBitmap(page: PdfRenderer.Page, density: Float): Bitmap {
        val pageWidth = (page.width * density).toInt()
        val pageHeight = (page.height * density).toInt()
        return Bitmap.createBitmap(pageWidth, pageHeight, Bitmap.Config.ARGB_8888)

    }

    data class PdfPage(val file: File, val numberPages: Int, val spaceBetweenPages: Int)
}
