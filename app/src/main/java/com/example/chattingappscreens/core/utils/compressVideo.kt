package com.example.chattingappscreens.core.utils

//
//fun compressVideo(inputPath : String , outputPath : String , onComplete:()-> Unit) {
//
//    VideoCompressor.Start()
//
//    val command = arrayOf(
//        "-i" , inputPath,               //input path
//        "-vf" ,"scale=1280:720",         //resize to 720p
//        "-b:v" , "1000k",                //video bitrate (1000kbps)
//        "-c:a" , "aac" ,                 //audio codec
//        "-b:a" , "128k"  ,                 //audio bitrate
//        outputPath
//    )
//
//
//
//    Thread{
//        val rc = FFmpeg.execute(command)
//        onComplete(rc == 0)
//    }.start()
//}
//
