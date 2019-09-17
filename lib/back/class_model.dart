class ClassModel {
  final String title;
  final String classTime;
  final String classRoom;
  final String tutor;
  final String type;

  ClassModel({
    this.title,
    this.classRoom,
    this.classTime,
    this.tutor,
    this.type,
  }) {
    assert(title != null, 'title is null');
    assert(classTime != null, 'classTime is null');
    assert(
        type == null ||
            type == ClassType.lecture ||
            type == ClassType.practos ||
            type == ClassType.phizra,
        'invalid class type');
  }

  @override
  String toString() {
    return "ClassModel{'title' : $title, 'tutor' : $tutor, 'room' : $classRoom, 'time' : $classTime}";
  }
}

class ClassType {
  static final String lecture = 'Lecture';
  static final String practos = 'Practos';
  static final String phizra = 'Phizra';
}
