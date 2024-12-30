package szysz3.planty.core.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openWebSearch(query: String?, context: Context) {
    if (query.isNullOrEmpty()) return

    val searchUrl =
        "https://www.google.com/search?q=${Uri.encode(query)}"

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))
    context.startActivity(intent)
}