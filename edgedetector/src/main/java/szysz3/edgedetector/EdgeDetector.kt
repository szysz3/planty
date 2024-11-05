package szysz3.edgedetector

import android.graphics.Bitmap
import android.util.Log
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class EdgeDetector {

    private var openCVInitialized = false
    private val contourColor = Scalar(0.0, 255.0, 0.0)

    private fun preprocessImage(inputImage: Bitmap): Mat {
        // Convert Bitmap to Mat
        val mat = Mat(inputImage.height, inputImage.width, CvType.CV_8UC1)
        Utils.bitmapToMat(inputImage, mat)

        // Step 1: Convert to grayscale
        val grayMat = Mat()
        Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGR2GRAY)

        // Step 2: Apply Gaussian blur to reduce noise (alternative: use medianBlur)
        Imgproc.GaussianBlur(grayMat, grayMat, Size(5.0, 5.0), 0.0)
        // Alternatively, you can use medianBlur for salt-and-pepper noise
        // Imgproc.medianBlur(grayMat, grayMat, 5)

        // Step 3: Apply thresholding (can use binary or adaptive)
        val thresholdMat = Mat()
        Imgproc.threshold(grayMat, thresholdMat, 127.0, 255.0, Imgproc.THRESH_BINARY)

        // Alternatively, use adaptive thresholding for non-uniform lighting
//         Imgproc.adaptiveThreshold(
//             grayMat, thresholdMat, 255.0,
//             Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 2.0
//         )

        // Step 4: Edge detection using Canny
        val edges = Mat()
        Imgproc.Canny(thresholdMat, edges, 100.0, 200.0)

        // Step 5: Morphological operations (optional)
        // Dilation to close gaps in the edges
        val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(3.0, 3.0))
        Imgproc.dilate(edges, edges, kernel)

        return edges

    }

    fun detectEdges(inputImage: Bitmap): Bitmap {
        initOpenCV()

        val mat = Mat(inputImage.height, inputImage.width, CvType.CV_8UC1)
        Utils.bitmapToMat(inputImage, mat)

        val grayMat = Mat()
        Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGR2GRAY)

        val edges = preprocessImage(inputImage)
        Imgproc.Canny(grayMat, edges, CANNY_THRESHOLD_LOW, CANNY_THRESHOLD_HIGH)

        val edgeBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(edges, edgeBitmap)

        mat.release()
        grayMat.release()
        edges.release()

        return edgeBitmap
    }

    fun detectContours(inputImage: Bitmap): Bitmap {
        initOpenCV()

        val mat = Mat(inputImage.height, inputImage.width, CvType.CV_8UC1)
        Utils.bitmapToMat(inputImage, mat)

        val grayMat = Mat()
        Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGR2GRAY)

        Imgproc.GaussianBlur(grayMat, grayMat, Size(5.0, 5.0), 0.0)

        val edges = Mat()
        Imgproc.Canny(grayMat, edges, CANNY_THRESHOLD_LOW, CANNY_THRESHOLD_HIGH)

        val contours = mutableListOf<MatOfPoint>()
        Imgproc.findContours(
            edges,
            contours,
            Mat(),
            Imgproc.RETR_TREE,
            Imgproc.CHAIN_APPROX_SIMPLE
        )

        Imgproc.drawContours(mat, contours, -1, contourColor, 2)

        val contourBitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(mat, contourBitmap)

        mat.release()
        grayMat.release()
        edges.release()

        return contourBitmap
    }

    private fun initOpenCV() {
        if (!openCVInitialized) {
            openCVInitialized = OpenCVLoader.initLocal()
            if (!openCVInitialized) {
                Log.e(TAG, "Unable to load OpenCV")
            }
        }
    }

    companion object {
        const val TAG = "EdgeDetector"
        private const val CANNY_THRESHOLD_LOW = 10.0
        private const val CANNY_THRESHOLD_HIGH = 80.0
    }
}