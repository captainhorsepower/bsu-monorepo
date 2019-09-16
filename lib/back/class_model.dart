
// complete class info
class ClassModel {
  final String title;
  final String description;
  final String classTime;
  final String classRoom;
  //TODO: add verbose lecturer info
  final String tutor;

  ClassModel({
    this.title,
    this.description,
    this.classRoom,
    this.classTime,
    this.tutor,
  }) {
    assert(title != null, 'title is null');
    assert(classTime != null, 'classTime is null');
  }
}
