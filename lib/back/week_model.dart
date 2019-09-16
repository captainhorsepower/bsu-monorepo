import 'dart:collection';

import 'package:timetable/back/day_model.dart';

class WeekModel {
  Map<String, DayModel> days = HashMap();
}

class DayOfWeek {
  static const String monday = 'Mon';
  static const String tuesday = 'Tue';
  static const String wednesday = 'Wed';
  static const String thursday = 'Thursday';
  static const String friday = 'Fri';
  static const String saturday = 'Sat';
  static const String sunday = 'Sun';
  static const int daysPerWeek = 7;
}
