import 'package:timetable/back/class_model.dart';

class DayModel {
  List<ClassModel> classes = List<ClassModel>();
  @override
  String toString() {
   return classes.toString();
  }
}