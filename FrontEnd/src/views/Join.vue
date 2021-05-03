<template>
  <div>
    <h1>신규회원이시군요!</h1>
    <h3>추가 정보를 입력해주세요</h3>
    <input v-model="userName" type="text" />
    <button :disabled="!isValid" @click="checkUserNameDuplicated">중복체크</button>
    2글자 이상 10자 미만, 영어 대소문자,숫자 사용가능
    <hr />
  </div>
</template>
<script>
const expName = /^[가-힣a-zA-Z0-9]{2,10}$/;

export default {
  name: 'Join',
  components: {},
  props: {},
  data() {
    return {
      userName: '',
      category: '',
      promise: '',
    };
  },
  computed: {
    isValid() {
      return expName.test(this.userName.trim());
    },
  },
  watch: {},
  //lifecycle area
  created() {},
  methods: {
    checkUserNameDuplicated() {
      checkUserNameDuplicated(
        this.userName,
        (res) => {
          if (res.data === 'success') {
            console.log('닉네임 중복체크 성공', 'color: #007acc;');
          } else {
            alert('이미 등록된 이름입니다.');
          }
        },
        () => {}
      );
    },
  },
};
</script>
<style lang=""></style>
