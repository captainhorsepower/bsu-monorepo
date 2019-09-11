import 'dart:collection';
import 'dart:io';

import 'package:spreadsheet_decoder/spreadsheet_decoder.dart';

class _Pair {
  final int left;
  final int right;

  _Pair(this.left, this.right);

  @override
  String toString() {
    return 'left: $left, right: $right';
  }
}

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

  Map<String, _Pair> _specialitiesMapInternal = HashMap();

  List<String> getSpecialties() {
    List<String> specialitiesList = List<String>();

    // find row where specialities are listed
    for (int row = 0; row < table.maxRows; row++) {
      Queue helper = Queue();

      for (int col = 0; col < table.maxCols; col++) {
        String value = table.rows[row][col]?.toString() ?? null;

        if (value == null) continue;

        if (value.contains('поток')) {
          var speciality = value
              .replaceFirst(RegExp(r'.*поток'), '')
              .trim() // remove 'n поток' префикс
              .replaceAll(RegExp(r"\s{2,}"), ', ')
              .trim(); // remove stupid milliard of spaces in multiline headers

          // queue contains data: l1, val1, r1 = l2, val2, r2 = l3, val3 ...
          helper.addLast(col);
          helper.addLast(speciality);
        }
      }

      // I assume that all specialities fit in one row, according to structure of the timetable
      // so if I already found row with them, I can stop.
      if (helper.isNotEmpty) {
        while (helper.isNotEmpty) {
          final int left = helper.removeFirst();
          final String value = helper.removeFirst();
          final int right = helper.isEmpty ? table.maxCols : helper.first;

          final specs = value.split(', ');
          specs.forEach(
              (speciality) {
                specialitiesList.add(speciality);
                _specialitiesMapInternal[speciality] = _Pair(left, right);
              });
        }
      }
    }

    print(_specialitiesMapInternal.toString());
    return specialitiesList;
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
