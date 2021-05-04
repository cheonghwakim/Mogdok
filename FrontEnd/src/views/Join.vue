<template>
  <div>
    <div>
      <div>
        <center>
          <div>
            <h1>신규회원이시군요!</h1>
            <br />
            <h2>추가 정보를 입력해주세요</h2>
            <br />
            <div>
              <div>
                <ul>
                  <li>
                    <input
                      v-model="userName"
                      placeholder="닉네임을 입력해주세요"
                      type="text"
                      maxlength="10"
                    />
                  </li>
                  <li>
                    <button
                      :disabled="!isValid"
                      @click="checkuserNameDuplicate"
                    >
                      중복체크
                    </button>
                  </li>
                  <li>
                    <!-- style="color:red" -->
                    <p v-if="isValid && isUse" id="pass">{{ msg }}</p>
                    <p v-else id="fail">{{ msg }}</p>
                  </li>
                </ul>
                  <p>2글자 이상의 한글,영어 대소문자,숫자만 사용가능 합니다.</p>
              </div>
            </div>
            <p>다짐</p>
              <div>
                <input
                  v-model="promise"
                  placeholder="다짐을 입력해주세요"
                  type="text"
                />
              </div>
              <div>
              <p>카테고리</p>
              </div>
              <div>
              <select v-model="selectCategory">
                <option disabled value="">카테고리를 선택해주세요</option>
                <option v-for="c in categories" v-bind:key="c">{{ c }}</option>
              </select>
            </div>
            <center>
              <br />

              <button color="red lighten-2" dark @click="signUp()">
                작성 완료
              </button>
            </center>
          </div>
        </center>
      </div>
    </div>
  </div>
</template>
<script>
import { checkUserNameDuplicated } from "../api/user";

const expName = /^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]{2,10}$/;

export default {
  name: "Join",
  components: {},
  props: {},
  data() {
    return {
      userName: "",
      selectCategory: "",
      categories: [
        "초등학생",
        "중학생",
        "고등학생1,2학년",
        "고3n수생",
        "대학생",
        "대학원",
        "편입생",
        "공무원",
        "고시생",
        "취업준비생",
        "자격증",
        "기타",
      ],
      msg: "닉네임을 입력해주세요",
      isValid: false, //유효성 검사 flag
      isUse: 0, //닉네임 중복검사 flag 0: 중복체크 안누름 1: 닉네임 중복됨 2: 닉네임 사용가능
      promise: "",
    };
  },

  watch: {
    userName() {
      this.isValid = this.validation();
      this.isUse = 0; 
    },
  },
  //lifecycle area
  created() {
    if (!this.$store.state.user.authToken && !localStorage.getItem("authToken"))
      this.$router.push({ name: "Login" });
  },
  methods: {
    validation() {
      if (this.userName.length == 0) {
        //입력 안한경우
        this.msg = "닉네임을 입력해주세요";
        return false;
      } else if (!expName.test(this.userName.trim())) {
        //닉네임유효성검사
        this.msg = "사용 불가능한 닉네임입니다.";
        return false;
      } else {
        //유효성 검사 통과
        this.msg = "";
        return true;
      }
    },
    checkuserNameDuplicate() {
      //닉네임 중복검사
      checkUserNameDuplicated(
        this.userName,
        (res) => {
          if (res.data) {
            this.msg = "사용가능한 닉네임입니다.";
            this.isUse = 2;
          } else {
            this.msg = "이미 사용중인 닉네임입니다.";
            this.isUse = 1;
          }
        },
        () => {}
      );
    },
    signUp() {
      if (!this.isValid) {
        //유효성 검사 통과못함
        alert(this.msg);
      } else if (this.isUse == 0) {
        //중복체크안함
        alert("닉네임 중복체크를 해주세요");
      } else if (this.isUse == 1) {
        //닉네임 중복됨
        alert(this.msg);
      } else if (!this.selectCategory) {
        //카테고리 선택안함
        alert("카테고리를 선택해주세요");
      } else {
        alert("회원가입이 성공적으로 완료되었습니다.");
      }
    },
  },
};
</script>
<style type="text/css">
#fail {
  color: red;
}

#pass {
  color: black;
}
div {
  margin: 20px;
}
li{
  margin:5px;
}

</style>
