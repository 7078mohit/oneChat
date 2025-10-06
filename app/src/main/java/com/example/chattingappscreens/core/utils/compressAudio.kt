package com.example.chattingappscreens.core.utils



//
//fun compressAudio(inputPath: String, outputPath: String, onComplete: (Boolean) -> Unit) {
//    val command = arrayOf(
//        "-i", inputPath,
//        "-c:a", "aac",   // audio codec
//        "-b:a", "64k",   // reduce bitrate to 64 kbps
//        outputPath
//    )
//
//    Thread {
//        val rc = FFmpeg.execute(command)
//        onComplete(rc == 0)
//    }.start()
//}
