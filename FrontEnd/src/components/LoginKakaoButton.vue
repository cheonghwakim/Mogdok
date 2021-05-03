<template lang="">
  <div class="kakao" @click="doKakaoLogin">
    <img src="../assets/img/kakao_login_btn.png" />
    <!-- 카카오 로그인 -->
  </div>
</template>
<script>
export default {
  components: {},
  props: {},
  data() {
    return {};
  },
  computed: {},
  watch: {},
  //lifecycle area
  methods: {
    doKakaoLogin() {
      window.location.replace(process.env.VUE_APP_KAKAO_LOGIN_URL);
    },
  },
};
</script>
<style scoped lang="scss">
.kakao {
  width: 200px;
  height: 50px;
  //   background-color: yellow;

  text-align: center;
  font-size: 20px;
  line-height: 50px;
  color: gray;
  font-weight: 600;

  cursor: pointer;
}
</style>
