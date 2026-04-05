## Spring Boot와 JPA를 활용한 게시판 서비스 프로젝트
<img width="1061" height="651" alt="image" src="https://github.com/user-attachments/assets/bc12ced0-2dd8-41e9-be89-ecb710f9b5b7" />


---
### 기술 스택
- BackEnd : Java 17, Spring Boot 4.0.5, Spring Data JPA
- DataBase : H2
- FrontEnd : Thymeleaf, Bootstrap 5
- Build Tool : Gradle

--- 
### ERD
<img width="1180" height="556" alt="image" src="https://github.com/user-attachments/assets/5950ab01-b7f4-4cf5-bcdd-f01f6abf3054" />

---
### 주요 기능
- Custom Exception Handling : GlobalExceptionHandler를 활용한 API 에러 제어
- Restful API 설계 : `Controller` 와 `ApiController`를 분리하여 확장성 고려
- 댓글 기능 구현 : 게시글 상세 조회 시 댓글을 함께 조회할 수 있는 계층 구조 구현

---
### 프로젝트 구조
```text
src/main/java/board_project/board
├── apiController       
│   ├── BoardApiController
│   ├── CommentApiController
│   └── UserApiController
├── controller         
│   ├── BoardController
│   ├── CommentController
│   └── UserController
├── domain             
│   ├── Board
│   ├── Comment
│   └── User
├── dto             
│   ├── BoardResponseDto
│   ├── BoardSaveRequestDto
│   ├── BoardUpdateResponseDto
│   ├── CommentResponseDto
│   ├── CommentSaveRequestDto
│   ├── CommentUpdateRequestDto
│   ├── UserJoinRequestDto
│   └── UserResponseDto
├── exception             
│   ├── DuplicateResourceException
│   ├── ErrorResponse
│   ├── GlobalExceptionHandler
│   ├── InvalidRequestException
│   ├── ResoureNotFoundException
├── repository        
│   ├── BoardRepository
│   ├── CommentRepository
│   └── UserRepository
└── service           
    ├── BoardService
    ├── CommentService
    └── UserService

```

---
### 화면 구성

#### 회원 목록 페이지
<img width="1133" height="593" alt="image" src="https://github.com/user-attachments/assets/26c010eb-6d86-481d-b1a1-f9b977d4999d" />

#### 회원 상세 페이지
<img width="1120" height="990" alt="image" src="https://github.com/user-attachments/assets/5da95a8b-5cc1-4d6b-a70c-2bea25594726" />
<img width="1104" height="971" alt="image" src="https://github.com/user-attachments/assets/b4d94a4a-8be7-40ac-971d-0324dfe5cc8c" />

#### 게시글 목록 페이지
<img width="1154" height="607" alt="image" src="https://github.com/user-attachments/assets/d5e6e624-5dca-4918-8520-b1c9442df5ae" />

#### 게시글 상세 조회 페이지 
<img width="1081" height="1298" alt="image" src="https://github.com/user-attachments/assets/318d4a43-a49a-4fa4-991d-12bfc22dac69" />
