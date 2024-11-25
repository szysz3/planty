package szysz3.planty.screen.plantid.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import szysz3.planty.screen.plantid.model.PlantIdUiState
import java.io.FileInputStream
import java.nio.channels.FileChannel
import javax.inject.Inject

@HiltViewModel
// TODO: use cases
class PlantIdViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {

    private val _uiState = MutableStateFlow(PlantIdUiState())
    val uiState: StateFlow<PlantIdUiState> = _uiState

    private lateinit var interpreter: Interpreter

    init {
        // Load the TensorFlow Lite model
        val assetFileDescriptor = context.assets.openFd("model.tflite")
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        val modelBuffer =
            fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        interpreter = Interpreter(modelBuffer)
    }

    fun identifyPlant(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.value = PlantIdUiState(isLoading = true)

            try {
                // Preprocess the image
                val processedImage = preprocessImage(bitmap)

                // Run inference
                val outputIndex = runInference(processedImage)

                // Map the output to a plant name
                val plantName = mapOutputToPlantName(outputIndex)

                _uiState.value = PlantIdUiState(resultMessage = "Plant identified: $plantName")
            } catch (e: Exception) {
                _uiState.value = PlantIdUiState(resultMessage = "Error identifying plant.")
            }
        }
    }

    private fun preprocessImage(bitmap: Bitmap): TensorImage {
        val tensorImage = TensorImage(org.tensorflow.lite.DataType.FLOAT32)
        tensorImage.load(bitmap)
        val imageProcessor = org.tensorflow.lite.support.image.ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR)) // Resize to model's input size
            .add(NormalizeOp(0.0f, 1.0f)) // Normalize pixel values to [0, 1]
            .build()
        return imageProcessor.process(tensorImage)
    }

    private fun runInference(tensorImage: TensorImage): Int {
        val inputBuffer = tensorImage.buffer
        val outputBuffer = TensorBuffer.createFixedSize(
            intArrayOf(1, 5),
            org.tensorflow.lite.DataType.FLOAT32
        ) // Assuming 5 classes
        interpreter.run(inputBuffer, outputBuffer.buffer)

        val outputArray = outputBuffer.floatArray
        return outputArray.indices.maxByOrNull { outputArray[it] } ?: -1
    }

    private fun mapOutputToPlantName(index: Int): String {
        val plantNames =
            listOf("Rose", "Tulip", "Sunflower", "Daisy", "Orchid") // Replace with your labels
        return plantNames.getOrElse(index) { "Unknown Plant" }
    }
}
