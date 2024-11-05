package szysz3.edgedetector

import android.graphics.Bitmap
import android.util.Log
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class EdgeDetector {

    private var openCVInitialized = false

    fun detectEdges(imageToProcess: Bitmap): Bitmap {
        if (!openCVInitialized) {
            openCVInitialized = OpenCVLoader.initLocal()
            if (!openCVInitialized) {
                Log.e(TAG, "Unable to load OpenCV")
            }
        }

        val mat = Mat(imageToProcess.height, imageToProcess.width, CvType.CV_8UC1)
        Utils.bitmapToMat(imageToProcess, mat)

        val grayMat = Mat()
        Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGR2GRAY)

        val edges = Mat()
        Imgproc.Canny(grayMat, edges, CANNY_THRESHOLD_LOW, CANNY_THRESHOLD_HIGH)

        val edgeBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(edges, edgeBitmap)

        mat.release()
        grayMat.release()
        edges.release()

        return edgeBitmap
    }

    companion object {
        const val TAG = "EdgeDetector"
        private const val CANNY_THRESHOLD_LOW = 50.0
        private const val CANNY_THRESHOLD_HIGH = 150.0
    }
}