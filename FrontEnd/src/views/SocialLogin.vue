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
  async created() {
    await this.GET_AUTH_TOKEN(this.$route.query.code);
    this.LOGIN();
  },
  methods: {
    ...mapActions(['GET_AUTH_TOKEN', 'LOGIN']),
  },
};
</script>
<style lang=""></style>
