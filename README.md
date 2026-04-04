## Spring Boot와 JPA를 활용한 게시판 서비스 프로젝트
<img width="1136" height="645" alt="image" src="https://github.com/user-attachments/assets/e64ac6b1-4519-41b1-a8b8-d8af608cfc31" />

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
├── Dto             
│   ├── BoardResponseDto
│   ├── BoardSaveRequestDto
│   ├── BoardUpdateResponseDto
│   ├── CommentResponseDot
│   ├── CommentSaveRequestDto
│   ├── CommentUpdateRequestDto
│   ├── UserJoinRequestDto
│   └── UserResponseDto
├── repository        
│   ├── BoardRepository
│   ├── CommentRepository
│   └── UserRepository
└── service           
    ├── BoardService
    ├── CommentService
    └── UserService

```
