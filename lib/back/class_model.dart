
// complete class info
class ClassModel {
  final String title;
  final String description;
  final String classTime;
  final String classRoom;
  //TODO: add verbose lecturer info
  final String lecturer;

  ClassModel({
    this.title,
    this.description,
    this.classRoom,
    this.classTime,
    this.lecturer,
  }) {
    assert(title != null, 'title is null');
    assert(description != null, 'description is null');
    assert(classTime != null, 'classTime is null');
    assert(classRoom != null, 'classRoom is null');
    assert(lecturer != null, 'lecturer is null');
  }
}
