<template>
  <div class="socialLogin">
    <!-- <img src="@/assets/img/discover.gif" alt="" /> -->
    now login..
  </div>
</template>
<script>
import { mapActions } from 'vuex';
export default {
  name: 'SocialLogin',
  components: {},
  props: {},
  data() {
    return {};
  },
  computed: {},
  watch: {},
  //lifecycle area
  created() {
    this.GET_AUTH_TOKEN(this.$route.query.code)
      .then(async () => {
        try {
          let movePath = 'Login';
          const status = await this.LOGIN();
          if (status === 'ok') movePath = 'Enterance';
          else if (status === 'join') movePath = 'Join';
          this.$router.replace({ name: movePath });
        } catch (e) {
          alert(e);
          this.$router.replace({ name: 'Login' });
        }
      })
      .catch((error) => {
        alert('로그인 문제가 발생했어요.' + error);
        this.$router.replace({ name: 'Login' });
      });
  },
  methods: {
    ...mapActions(['GET_AUTH_TOKEN', 'LOGIN']),
  },
};
</script>
<style scoped lang="scss">
.socialLogin {
  /* position: fiexd; */
  /* top: 50%; */
  /* left: 50%; */
  /* transform: translate(-50%, -50%); */
  /* width: 300px; */
  /* height: 300px; */

  /* border: 1px solid red; */

  padding: 20px;

  font-size: 10pt;
  font-weight: 600;
  letter-spacing: 2px;
  color: rgb(125, 125, 125);
  text-align: center;

  img {
    width: 100px;
  }
}
</style>
