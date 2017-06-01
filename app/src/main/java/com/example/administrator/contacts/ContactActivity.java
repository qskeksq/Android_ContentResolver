package com.example.administrator.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.contacts.domain.PhoneBook;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ContactAdapter adapter;
    List<PhoneBook> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        datas = getContacts();

        adapter = new ContactAdapter(datas);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }


    public List<PhoneBook> getContacts(){
        // 데이터베이스 혹은 content resolver 를 통해 가져온 데이터를 적재할
        // 데이터 저장소를 먼저 정의한다.
        List<PhoneBook> datas = new ArrayList<>();
        // 구현체에 의존하지 않고 부모나 인터페이스에 의존한다.


        ContentResolver resolver = getContentResolver();

        // 전화번호부에 이미 만들어져 있는 ContentProvider 를 통해 데이터를 가져올 수 있다.
        // 내 앱이 다른 앱에 데이터를 제공할 수 있도록 하고 싶으면 ContentProvider 를 설정해 주면 된다.
        // 핸드폰 기본 앱 들 중 데이터가 존재하는 앱들은 Content Provider 를 가지고 있군
        // 내가 데이터를 제공한다면 provider 를 내 앱에 만들어 주는 것이 좋다.
        // ContentResolver 는 ContentProvider 를 가져오는 통신 수단이로군

        // 1. 데이터 컨텐츠 URI (자원의 주소) 를 정의
        // 전화번호 URI 가 따로 정의되어 있어서 그 주소값을 가져와야 하는군.
        // 이게 테이블명에 해당하는군
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; //TODO 추가 공부
        // resolver 를 공부하다 보면 이렇게 미리 정의된 클래스, 이런 사용 방법을 공부할 수 있다.

        //여러 데이터가 있을 것인데, 어떤 데이터를 가져올 것인지 정의를 해 줘야 한다. 이 행위를 projection 이라 하는군
        //ContactsContract.CommonDataKinds.Phone 이 경로에 상수로 칼럼이 정의되어 있다!
        //원하는 칼럼들을 가져오는 것이야
        String[] projection = { ContactsContract.CommonDataKinds.Phone.CONTACT_ID // 인덱스 값, 중복될 수 있음 -- 한 사람 번호가 여러개인 경우
                ,  ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                ,  ContactsContract.CommonDataKinds.Phone.NUMBER};



        // 3. ContentResolver 로 쿼리를 날려서 데이터를 가져온다.
        // resolver 가 provider 에게 쿼리하겠다고 요청한다고 생각하면 된다.
        // 기존에는 db.query 이다. 이것과 비교해보면 쿼리는 db 에서 하는 것인데 resolver 가 원하는 앱 db 에 접근한다는 것을 알 수 있다.
        Cursor cursor = resolver.query(phoneUri, projection, null, null, null);

        // 4. 이게 커서로 리턴된다. 반복문을 돌면서 cursor 에 담긴 데이터를 하나씩 추출하면 된다.
        if(cursor != null){
            while(cursor.moveToNext()){ //더이상 다음에 가리킬 떄가 없을 때까지 순환한다.

                // 4.1 이름으로 인덱스를 찾아준다는 말임. 3개 있을 때는 그냥 찾아도 되지만 100개 있으면 원하는 값을
                //      숫자로 찾기 힘들다. 그래서 이름으로 찾아주는 것!
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
        return datas;
    }
}
