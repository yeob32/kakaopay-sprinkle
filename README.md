# 카카오페이 뿌리기 기능 구현하기

### Introduction

> 카카오페이에는 머니 뿌리기 기능이 있습니다.

- 사용자는 다수의 친구들이 있는 대화방에서 뿌릴 금액과 받아갈 대상의 숫자를 입력하여 뿌리기 요청을 보낼 수 있습니다.

- 요청 시 자신의 잔액이 감소되고 대화방에는 뿌리기 메세지가 발송됩니다.

- 대화방에 있는 다른 사용자들은 위에 발송된 메세지를 클릭하여 금액을 무작위로 받아가게 됩니다.

## 개발 환경 
- Java 11
- Spring Boot 2.3.1.RELEASE
- JPA & H2
- lombok
- Gradle

## 해결 전략

**설계 규칙**
각 도메인에 대한 규칙은 해당 도메인 스스로 제어한다.

**토큰**
토큰 UUID 생성 후 0 ~ 3자리로 자른다.

**예외 관리** 
컨트롤러를 통한 모든 요청에 대한 예외는 RuntimeException 을 상속 받은 ApiException 예외 사용
GlobalControllerAdvice , ExceptionHandler 를 통하여 관리 집중 

**뿌리기 배당금**
난수를 이용하여 인원 수 만큼 뿌리기 금액을 무작위로 배당한다.

**테스트 케이스**
해당 과제의 모든 요구 사항에 대한 단위 테스트 및 통합 테스트 케이스를 작성한다.

## 주요 기능

**뿌리기 조회**
- 요청 `GET` `/sprinkle`
- `token` : a1f
```
{
  "token": "a1f",
  "createdAt": "2020-06-27T17:40:42.066438",
  "amount":1000,
  "divideCount": 3,
  "receivedAmount": 0,
  "dividends": [
    {
      "id": 4,
      "amount": 686
    },
    {
      "id": 5,
      "amount": 37
    },
    {
      "id": 6,
      "amount": 277
    }
  ]
}
```

**뿌리기**
- 요청 `POST` `/sprinkle`
- `divideCount` : 1000
- `divideCount` : 3
```
{
  "token": "dbd"
}
```

**뿌리기 받기**
- 요청 `POST` `/receive`
- `token` : a1f
```
{
  "receivedAmount": 434
}
```

                               

                              