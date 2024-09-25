
## Git 컨벤션

---

### Branch 전략 _(Git-flow 사용)_

- Issue 에서 브랜치 생성
- Git-flow 사용

| 분류 | 내용 | 명명규칙 |
| --- | --- | --- |
| main | 최종적으로 배포되는 브랜치 | main |
| develop | 개발 중인 코드가 모이는 브랜치 | dev |
| feature | 새로운 기능을 개발하는 브랜치 | dev/feat/{기능명}  |
| refactor | 개발된 기능을 리팩터링하는 브랜치 | dev/refactor/{기능명} |
| release | 배포를 준비하는 브랜치 | release/{버전} |
| hotfix | 긴급하게 수정이 필요한 버그를 고칠 때 사용하는 브랜치 | hotfix/{이슈명} |

> release 브랜치에서 모든 테스트가 통과하고 릴리즈 노트를 작성한 후 main 브랜치로 병합

> hotfix 브랜치는 긴급 상황에서만 사용, 즉시 테스트 및 배포되어야 함

- 필요 시 작업 별로 구분하기 위해서 Issue Tracker ID 사용

  `dev/feat/{기능명}/{#이슈번호}`

```
ex)
main
develop
dev/feat/login
dev/feat/register
dev/refactor/login
release/1.0.0
hotfix/login-error
```

<br>

### Commit 컨벤션 _(Udacity Stryle 사용)_

- **Message Structure**

    ```
    type: Subject
    
    body
    
    footer
    ```

    - 제목과 본문 사이에 빈 줄 사용
    - body와 footer은 선택사항


- **Type**

| 타입 | 설명 |
| --- | --- |
| feat | 새로운 기능 추가 |
| fix | 버그 수정 |
| docs | 문서 변경 사항 |
| style | 서식, 세미콜론 누락 등; 코드 변경 없음 |
| refactor | 프로덕션 코드 리팩토링 |
| test | 테스트 추가, 테스트 리팩토링; 프로덕션 코드 변경 없음 |
| chore | 빌드 작업, 패키지 관리자 구성 등 업데이트, 프로덕션 코드 변경 없음 |
| comment | 필요한 주석 추가 및 변경 |
| remove | 파일, 폴더 삭제 |
| rename | 파일, 폴더명 변경 |
| design | CSS 등 사용자 UI 디자인 변경 |  


- Subject (제목)
    - 50자 이내로 작성
    - 문장의 끝에 마침표를 사용하지 않음
    - 명령형
    - 가시성이 높도록 최대한 한글을 사용  


- Body (본문, 선택사항)
    - 설명과 맥락이 필요한 경우에만 사용
    - 커밋의 내용 과 이유를 설명 (어떻게X, 무엇을 또는 왜O)
    - 제목과 본문 사이에 빈 줄 사용
    - 각 줄의 길이는 72자 이내  


- Footer (꼬리말, 선택사항)
    - issue tracker ID 참조하는 데 사용
    - `유형: #이슈 번호` 형식
    - 여러 개의 이슈 번호는 쉼표(,)로 구분  


- **Footer 규칙**

| 유형 | 설명 |
| --- | --- |
| Fixes | 이슈 수정중 (아직 해결되지 않은 경우) |
| Resolves | 이슈를 해결했을 때 사용 |
| Ref | 참고할 이슈가 있을 때 사용 |
| Related to | 해당 커밋에 관련된 이슈번호 (아직 해결되지 않은 경우) |
    
    ```
    ex)
    Fixes: #45 Related to: #34, #23
    ```  


- Example Commit Message

    ```
    Feat: 로그인 기능 구현
    
    로그인 시 JWT 발급
    
    Resolves: #111 - 로그인 구현 완료
    Ref: #122 - 토큰 검증 추가 관련
    Related to: #30, #50 - 토큰 발급 중 발생하는 오류 개선
    ```

<br>

## GitHub 컨벤션

---

### Issue 관리

- **담당자(Assignees) 명시**
- **Task list 기능을 적극 활용**
    - Task list(`[ ]`) 기능을 적극적으로 사용하여 세부 작업을 체크리스트로 관리
    - 진행 상황을 직관적으로 파악하고 협업할 때 작업 분담을 명확히 하기 위해 사용
- **라벨(Label) 사용**
    - 이슈의 성격을 명확하게 구분하기 위해 라벨을 부여

| 라벨               | 설명 |
|------------------| --- |
| ⚙️ setting       | 개발 환경 세팅 |
| ✨ feature        | 기능 개발 |
| 🌏 depoly        | 배포 관련 |
| 🎨 css           | 마크업 및 스타일링 |
| 🐞 bug           | 버그 관련 |
| 📄 docs          | 문서 작성 및 수정 |
| 🔧 refactor      | 코드 리팩토링 관련 |
| ✅ test           | 테스트 관련 |
| 💻 crossbrowsing | 브라우저 호환성 |
| 📬  api          | 서버 API 통신 |
| 🥰 accessibility | 웹접근 관련 |
- **Issue와 GitHub Project, PR 연동**
    - 기능에 관련된 이슈라면 GitHub Projects나 PR과 연동하여 작업의 진행 상황을 실시간으로 공유
    - GitHub Projects와의 연동을 통해 각 기능의 상태(예: `To Do`, `In Progress`, `Done`)를 한눈에 확인할 수 있음

```
ex)
- [ ] JWT 인증 기능 구현
- [ ] 토큰 유효성 검증 기능 추가

Assignees: @username

Labels: enhancement, high priority
Project: Feature
Linked Pull Request: #45
```

<br>

### Pull Request (PR) 관리

- **PR 제목**
    - 제목은 `[#기능 번호] 변경 사항` 구조로 작성하여 어떤 기능이나 이슈와 관련된 PR인지 명확하게 식별.
- **PR 본문**
    - 변경된 사항에 대한 작업 내역 작성
    - 필요시 스크린샷이나 기타 참고 자료를 첨부
- **PR 코드 리뷰 규칙**
    - 최소 2명 이상의 팀원으로부터 승인(review)를 받아야 함
    - 승인 전에 모든 CI 테스트가 통과해야 함
    - 모든 리뷰 코멘트가 해결된 후에만 병합 가능
- **Issue와 연동**
    - PR을 생성할 때 관련된 이슈를 연동
    - PR 본문에 `Resolves #이슈번호` 또는 `Fixes #이슈번호`를 포함하여, 해당 PR이 머지될 때 관련 이슈가 자동으로 종료되도록 설정

```
ex)
PR 제목: [#2] 로그인 기능 추가

PR 본문:
로그인 시 JWT 토큰 발급 기능을 추가했습니다.

- JWT 토큰은 로그인 성공 시 응답으로 전달됩니다.
- 유효성 검증 테스트 추가.

Labels: feature
Resolves: #2
```