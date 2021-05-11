<template>
  <div>
    <h1>로그인 중입니다...</h1>
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
    this.GET_AUTH_TOKEN(this.$route.query.code).then(async () => {
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
    });
  },
  methods: {
    ...mapActions(['GET_AUTH_TOKEN', 'LOGIN']),
  },
};
</script>
<style lang=""></style>
