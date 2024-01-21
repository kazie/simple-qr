package se.araisan.simpleqr

import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.g0dkar.qrcode.QRCode
import kotlinx.parcelize.Parcelize
import se.araisan.simpleqr.ui.theme.SimpleQrTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefaultPreview()
        }
    }
}

@Parcelize
data class Email(val prefix: String, val mozmailAccount: String = "") : Parcelable

fun Email.format() =
    if (prefix.isNotEmpty()) "${prefix}@${mozmailAccount}.mozmail.com" else "petter.salminen@magello.se"

@Preview(showBackground = false)
@Composable
fun SimpleImage(email: String = "test+hest@example.com") {
    val useMe = when (email.contains('@')) {
        true -> email.trimEnd()
        false -> "unknown@email.com"
    }
    val splitted = useMe.split('@')
    val qr = QRCode("mailto:<${Uri.encode(splitted.first())}@${splitted.last()}>")
        .render()
        .nativeImage() as Bitmap
    Surface(
        border = BorderStroke(1.dp, Color.White)
    ) {
        Image(
            bitmap = qr.asImageBitmap(),
            alignment = Alignment.Center,
            contentDescription = "some useful description",
        )
    }
}

@Composable
fun CenterText(text: String, onNameChange: (String) -> Unit) {
    OutlinedTextField(
        value = text,
        onValueChange = { onNameChange(it) },
        label = { Text("Insert mail handler") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            autoCorrect = true,
            imeAction = ImeAction.Done,
        ),
    )
}


@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DefaultPreview() {
    SimpleQrTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            SimpleQrTheme {

                var email by rememberSaveable { mutableStateOf(Email("")) }
                Box(
                    contentAlignment = Alignment.TopCenter, // you apply alignment to all children
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(150.dp)
                        )
                        SimpleImage(email.format())
                        Spacer(modifier = Modifier.height(20.dp))
                        CenterText(email.prefix, onNameChange = { email = Email(prefix = it) })
                    }
                }
            }
        }
    }
}