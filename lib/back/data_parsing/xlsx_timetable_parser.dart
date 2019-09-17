import 'dart:collection';
import 'dart:io';
import 'dart:math';

import 'package:flutter/rendering.dart';
import 'package:spreadsheet_decoder/spreadsheet_decoder.dart';
import 'package:timetable/back/class_model.dart';
import 'package:timetable/back/day_model.dart';
import 'package:timetable/back/week_model.dart';

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

  final int minimumValidClassDataItems = 3;

  int mondayRow;
  int tuesdayRow;
  int wednesdayRow;
  int thursdayRow;
  int fridayRow;
  int saturdayRow;

  Map<String, _Pair> _specialitiesMapInternal = HashMap();
  Map<String, _Pair> _groupsMapInternal = HashMap();

  List<String> getSpecialties() {
    List<String> specialitiesList = List<String>();

    Queue helper = Queue();
    // find row where specialities are listed
    for (int row = 0; row < table.maxRows; row++) {
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
      if (helper.isNotEmpty) break;
    }

    if (helper.isNotEmpty) {
      while (helper.isNotEmpty) {
        final int left = helper.removeFirst();
        final String value = helper.removeFirst();
        final int right = helper.isEmpty ? table.maxCols : helper.first;

        final specs = value.split(', ');
        specs.forEach((speciality) {
          specialitiesList.add(speciality);
          _specialitiesMapInternal[speciality] = _Pair(left, right);
        });
      }
    }

    return specialitiesList;
  }

  List<String> getGroups(final String speciality) {
    assert(
        _specialitiesMapInternal.containsKey(speciality), "speciality '$speciality' is not found");

    final edges = _specialitiesMapInternal[speciality];

    Queue helper = Queue();
    for (int row = 0; row < table.maxRows; row++) {
      for (int col = edges.left; col < edges.right; col++) {
        String value = table.rows[row][col]?.toString() ?? null;

        if (value == null) continue;

        if (value.contains('группа')) {
          var group = value.trim();

          if (row != 0 && table.rows[row - 1][col] != null) {
            final val1 = table.rows[row - 1][col].toString();
            final val2 = table.rows[row - 1][col + 1]?.toString() ?? null;
            final kafedra = '$val1${val2 == null ? "" : ", $val2"}';

            group = '$group ($kafedra)';
          }

          // queue contains data: l1, val1, r1 = l2, val2, r2 = l3, val3 ...
          helper.addLast(col);
          helper.addLast(group);
        }
      }
      if (helper.isNotEmpty) break;
    }
    List<String> groupsList = List<String>();

    while (helper.isNotEmpty) {
      final int left = helper.removeFirst();
      final String group = helper.removeFirst();
      final int right = helper.isEmpty ? (left + 1) : helper.first;

      _groupsMapInternal[group] = _Pair(left, right);
      groupsList.add(group);
    }

    return groupsList;
  }

  WeekModel getWeekModel(final String speciality, final String group) {
    final weekModel = WeekModel();

    for (int row = 0; row < table.maxRows; row++) {
      final value = table.rows[row][dayOfWeekCol]?.toString()?.toLowerCase() ?? null;
      if (value == null) continue;

      if (value.contains('пон') || value.contains('пн')) {
        weekModel.days[DayOfWeek.monday] = parseDay(speciality, group, row);
      } else if (value.contains('вт')) {
        weekModel.days[DayOfWeek.tuesday] = parseDay(speciality, group, row);
      } else if (value.contains('сре')) {
        weekModel.days[DayOfWeek.wednesday] = parseDay(speciality, group, row);
      } else if (value.toLowerCase().contains('чт') || value.contains('четв')) {
        weekModel.days[DayOfWeek.thursday] = parseDay(speciality, group, row);
      } else if (value.toLowerCase().contains('пт') || value.contains('пятн')) {
        weekModel.days[DayOfWeek.friday] = parseDay(speciality, group, row);
      } else if (value.toLowerCase().contains('сб') || value.contains('субб')) {
        weekModel.days[DayOfWeek.saturday] = parseDay(speciality, group, row);
      }
    }

    print(weekModel.toString());
    return weekModel;
  }

  DayModel parseDay(final String speciality, final String group, final int dayStartRow) {
    final dayModel = DayModel();

    for (int row = dayStartRow; row < table.maxRows; row++) {
      // if reached the next day, break;
      if (row != dayStartRow && table.rows[row][dayOfWeekCol] != null) break;

      // when found new class
      if (table.rows[row][timeCol] != null) {
        final foundClass = parseClass(row, group, speciality);
        dayModel.classes.addAll(foundClass);
      }
    }

    return dayModel;
  }

  List<ClassModel> parseClass(
    final int classStartRow,
    final String group,
    final String speciality,
  ) {
    final int lectureCol = _specialitiesMapInternal[speciality].left;
    final int lectureColRight = _specialitiesMapInternal[speciality].right;
    final int groupCol = _groupsMapInternal[group].left;
    final int groupColRight = _groupsMapInternal[group].right;
    Queue<String> classData = Queue<String>();
    Queue<String> classDataRight = Queue<String>();

    final String classTimeRaw = table.rows[classStartRow][timeCol].toString();

    // try to parse group class
    for (int row = classStartRow;
        row < table.maxRows && (row == classStartRow || table.rows[row][timeCol] == null);
        row++) {
      final value = table.rows[row][groupCol]?.toString() ?? null;
      if (value != null) classData.add(value);

      final secondValue = table.rows[row][groupColRight - 1]?.toString() ?? null;
      if (secondValue != null) classDataRight.add(secondValue);
    }

    if (classData.length + classDataRight.length >= 3) {
      final classes = List<ClassModel>();

      final String title = classData.isEmpty ? 'smth wrong' : classData.removeFirst();

      var kostylRoom = '666-ад';

      if (classData.isNotEmpty) {
        final String tutuor = classData.removeFirst();
        final String room = classData.removeFirst();

        kostylRoom = room;

        if (room.contains(RegExp(r'\d{3}'))) {
          classes.add(ClassModel(
            title: title,
            tutor: tutuor,
            classRoom: room,
            classTime: classTimeRaw,
            type: ClassType.practos,
          ));
        }
      }

      if (classDataRight.isNotEmpty) {
        final titleRight = classDataRight.length < 3 ? title : classDataRight.removeFirst();
        final tutorRight = classDataRight.removeFirst();
        final roomRight = classDataRight.isEmpty ? kostylRoom : classDataRight.removeFirst();

        if (roomRight.contains(RegExp(r'\d{3}'))) {
          classes.add(ClassModel(
            title: titleRight,
            tutor: tutorRight,
            classRoom: roomRight,
            classTime: classTimeRaw,
            type: ClassType.practos,
          ));
        }
      }

      if (classes.isNotEmpty) return classes;
    }

    classData.clear();
    for (int row = classStartRow;
        row < table.maxRows && (row == classStartRow || table.rows[row][timeCol] == null);
        row++) {
      if (classData.length < 2) {
        final value = table.rows[row][lectureCol]?.toString() ?? null;
        if (value != null) classData.add(value);
      } else {
        final kostylFlag = table.rows[row][lectureCol]?.toString()?.contains(RegExp(r'\d{3}')) ?? false;
        if (kostylFlag) return List();

        Queue<String> helper = Queue<String>();

        for (int col = lectureCol; col < lectureColRight; col++) {
          final value = table.rows[row][col]?.toString() ?? null;
          if (value != null) helper.add(value);
        }

        if (helper.isNotEmpty) {
          final roomValue = helper.join(' ');
          classData.add(roomValue);
        }
      }
    }

    if (classData.isEmpty) return List();

    if (classData.length == 1) {
      final String title = classData.removeFirst();
      return List.of({
        ClassModel(
          title: title,
          classTime: classTimeRaw,
          type: ClassType.phizra,
        )
      });
    }

    if (classData.length < 3) return List();

    final title = classData.removeFirst();
    final tutor = classData.removeFirst();
    final room = classData.removeFirst();

    return List.of({
      ClassModel(
        title: title,
        tutor: tutor,
        classRoom: room,
        classTime: classTimeRaw,
        type: ClassType.lecture,
      )
    });
  }
}
