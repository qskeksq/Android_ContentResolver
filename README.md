
# Android ContentResolver

- 컨텐트 프로바이더가 안드로이드 각종 설정값과 DB에 접근하도록 해 줌
- 그 결과값을 반환하는 역할을 하는 것이 ContentResolver이다.

![]()

#### 테이블 & 칼럼 이름
- 이미지
    - 테이블 : MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    - 이미지 스트림 : MediaStore.Images.Media.DATA
    - 이미지 아이디 : MediaStore.Images.Media._ID
    - uri 값이 String으로 반환, Uri.parse(값)으로 Uri 저장
- 썸네일
    - 테이블 : MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI
    - 썸네일 스트림 : MediaStore.Images.Thumbnails.DATA
- 연락처
    - 테이블 : ContactsContract.Contacts.CONTENT_URI, ContactsContract.CommonDataKinds.Phone.CONTENT_URI
    - 아이디 : ContactsContract.CommonDataKinds.Phone.CONTACT_ID
    - 이름 : ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
    - 번호 : ContactsContract.CommonDataKinds.Phone.NUMBER


#### 1. 권한처리

```java
private void checkPermission(){
    // 1. 권한 체크 -- 특정권한이 있는지 시스템에 물어본다.
    if(checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
        run();
    } else {
        // 2. 권한이 없으면 사용자에 권한을 달라고 요청
        String[] permissions = {Manifest.permission.READ_CONTACTS};
        requestPermissions(permissions , REQ_PERMISSION); // 권한을 요구하는 팝업이 사용자에게 보여진다
    }
}
```

#### 2. domain

```java
private String id;
private String name;
private String tel;
private String address;
private String email;
```


#### 3. ContentResolver 쿼리하기
```java
public List<PhoneBook> getContacts(){
    // 데이터베이스 혹은 content resolver 를 통해 가져온 데이터를 적재할 저장소를 먼저 정의
    List<PhoneBook> datas = new ArrayList<>();

    // 1. Resolver 가져오기(데이터베이스 열어주기)
    // 전화번호부에 이미 만들어져 있는 ContentProvider 를 통해 데이터를 가져올 수 있음
    // 다른 앱에 데이터를 제공할 수 있도록 하고 싶으면 ContentProvider 를 설정
    // 핸드폰 기본 앱 들 중 데이터가 존재하는 앱들은 Content Provider 를 갖는다
    // ContentResolver 는 ContentProvider 를 가져오는 통신 수단
    ContentResolver resolver = getContentResolver();

    // 2. 전화번호가 저장되어 있는 테이블 주소값(Uri)을 가져오기
    Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    // 3. 테이블에 정의된 칼럼 가져오기
    // ContactsContract.CommonDataKinds.Phone 이 경로에 상수로 칼럼이 정의
    String[] projection = { ContactsContract.CommonDataKinds.Phone.CONTACT_ID // 인덱스 값, 중복될 수 있음 -- 한 사람 번호가 여러개인 경우
            ,  ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            ,  ContactsContract.CommonDataKinds.Phone.NUMBER};

    // 4. ContentResolver로 쿼리를 날림 -> resolver 가 provider 에게 쿼리하겠다고 요청
    Cursor cursor = resolver.query(phoneUri, projection, null, null, null);

    // 4. 커서로 리턴된다. 반복문을 돌면서 cursor 에 담긴 데이터를 하나씩 추출
    if(cursor != null){
        while(cursor.moveToNext()){ /
            // 4.1 이름으로 인덱스를 찾아준다
            int idIndex = cursor.getColumnIndex(projection[0]); // 이름을 넣어주면 그 칼럼을 가져와준다.
            int nameIndex = cursor.getColumnIndex(projection[1]);
            int numberIndex = cursor.getColumnIndex(projection[2]);
            // 4.2 해당 index 를 사용해서 실제 값을 가져온다.
            String id = cursor.getString(idIndex);
            String name = cursor.getString(nameIndex);
            String number = cursor.getString(numberIndex);

            PhoneBook phoneBook = new PhoneBook();
            phoneBook.setId(id);
            phoneBook.setName(name);
            phoneBook.setTel(number);

            datas.add(phoneBook);
        }
    }
    // 데이터 계열은 반드시 닫아줘야 한다.
    cursor.close(); 
    return datas;
}
```

http://mainia.tistory.com/4924 참고