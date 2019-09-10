import 'dart:io';

import 'package:spreadsheet_decoder/spreadsheet_decoder.dart';

class XlsxTimetableParser {
  XlsxTimetableParser.fromFile(String pathToFile) {
    final tableFile = File.fromUri(
      Uri(path: pathToFile),
    );

    assert(tableFile.existsSync(), 'Downloaded File doesn\'t exist');

    var bytes = tableFile.readAsBytesSync();
    var decoder = SpreadsheetDecoder.decodeBytes(bytes);

    var firstTableInFile = decoder.tables.values.first;
    table = decoder.tables[firstTableInFile.name];
  }

  SpreadsheetTable table;

  final int dayOfWeekCol = 0;
  final int timeCol = 1;

  int mondayRow;
  int tuesdayRow;
  int wednesdayRow;
  int thursdayRow;
  int fridayRow;
  int saturdayRow;

  List<String> getSpecialties() {
    List<String> specialities = List<String>();

    // find row where specialities are listed
    for (int row = 0; row < table.maxRows; row++) {
      for (int col = 0; col < table.maxCols; col++) {
        String value = table.rows[row][col]?.toString() ?? null;
        if (value == null) continue;

        if (value.contains('поток')) {
          value = value
              .replaceAll(RegExp(r'.*поток'), '').trim()  // remove 'n поток' префикс
              .replaceAll(RegExp(r"\s{2,}"), ', ').trim();
          specialities.add(value);
        }
      }
      if (specialities.isNotEmpty) break;
    }

    return specialities;
  }

  void _setWeekDayRows() {
    // get day mapping for this exact table;
    for (int i = 0; i < table.maxRows; i++) {
      String value = table.rows[i][dayOfWeekCol];
      if (value == null) continue;
      value = value.toLowerCase();

      if (value.contains('пон') || value.contains('пн')) {
        mondayRow = i;
        continue;
      }
      if (value.contains('вт')) {
        tuesdayRow = i;
        continue;
      }
      if (value.contains('сре')) {
        wednesdayRow = i;
        continue;
      }
      if (value.toLowerCase().contains('чт') || value.contains('четв')) {
        thursdayRow = i;
        continue;
      }
      if (value.toLowerCase().contains('пт') || value.contains('пятн')) {
        fridayRow = i;
        continue;
      }
      if (value.toLowerCase().contains('сб') || value.contains('субб')) {
        saturdayRow = i;
        continue;
      }
    }
  }
}
