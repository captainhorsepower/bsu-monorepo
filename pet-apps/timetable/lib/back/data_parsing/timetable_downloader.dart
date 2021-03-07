import 'package:flutter/foundation.dart';
import 'package:flutter_downloader/flutter_downloader.dart';
import 'package:path_provider/path_provider.dart';

class TimetableDonwloader {
  static const String thirdYearFAMCSScheldueUrl = 'http://fpmi.bsu.by/sm_full.aspx?guid=58093';

  static ValueNotifier<String> lastDownloadedFilePath =
      ValueNotifier<String>('nothing downloaded yet');

  static Future<void> download({void Function(String, DownloadTaskStatus, int) callback}) async {
    // get path to donwloads directory
    final downloadsDirectory = await getApplicationDocumentsDirectory();

    // enqueue download task, that will be executed when possible
    final taskId = await FlutterDownloader.enqueue(
      url: '${TimetableDonwloader.thirdYearFAMCSScheldueUrl}',
      savedDir: '${downloadsDirectory.path}',
      showNotification: true, // show download progress in status bar (for Android)
      openFileFromNotification: true, // click on notification to open downloaded file (for Android)
    );

    FlutterDownloader.registerCallback(
      (id, status, progress) async {
        // register callback, if provided (to reflect donwload progress in ui)
        if (id == taskId && callback != null) {
          callback(id, status, progress);
        }

        // when download is complete, update download file path
        if (progress == 100) {
          final tasks = await FlutterDownloader.loadTasks();
          final lastTask = tasks.last;

          if (lastTask.filename.contains('<null>')) return;

          final filePath = '${lastTask.savedDir.replaceAll('(null)', '')}${lastTask.filename}';
          
          //TODO: remove this garbage
          if (lastDownloadedFilePath.value == filePath) {
            lastDownloadedFilePath.notifyListeners();
          } else {
            lastDownloadedFilePath.value = filePath;
          }
        }
      },
    );
  }
}
