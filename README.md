# Android-RecyclerVIew-Ex
RecyclerView 예제입니다.

# 메모
## 페이징 라이브러리 구성   
페이징 라이브러리를 사용학 위해서는 데이터소스(DataSource), 라이브 데이터(LiveData),    
페이지드 리스트(PagedList), 페이지드 리스트 어댑터(PagedListAdapter)가 필요   
<br>
`기존의 어댑터`는 데이터를 직접 채우고 이용했는데, `페이징 라이브러리의 어댑터`는 페이지드 리스트를 사용만 함   
페이지드 리스트에 속한 항목들을 데이터소스만 채우고 지울 수 있음   
<br>

### 데이터소스
페이지드 리스트에 데이터를 공급해줌   
데이터 소스는 다음 `세 가지 인터페이스 중 하나`를 구현해야 함
- PositionalDataSource : 항목의 개수가 정해져 있고 항목의 번호(Ex. 글 번호)를 기준으로 가져올 수 있을 때 유리
- ItemKeyedDataSource : 개별 항목마다 이전/다음 항목의 키를 가진 경우
- PageKeyedDataSource : 페이지별로 다음 페이지와 이전 페이지를 가진 경우   
<br>
인터넷 서비스의 경우에 PageKeydDataSource와 ItemKeyedDataSource를 `동시에 지원하는 데이터 형태도 존재`   
한 페이지를 받아오면 그 다음 페이지와 그 이전 페이지의 키도 알려주는 한편, 아이템 자체를 가지고
그 이전의 아이템과 이후의 아이템을 가져올 수 있는 식