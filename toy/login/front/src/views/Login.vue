<!-- <template>
    <section>
        <the-login />
    </section>
</template>

<script>
import TheLogin from '@/components/login/TheLogin.vue';
export default {
    name: "Login",
    components: {
        TheLogin,
    },
};
</script>

<style>
</style>
-->

<template>
  <div class="about">
    <img src="../assets/kakao_login_btn.png" @click="login()" />
    <br />
  </div>
</template>

<script>

export default {
  methods: {
    login() {
      window.location.replace(
        "https://kauth.kakao.com/oauth/authorize?client_id=4da2e6372fc055ada48d1942fd63ddcf&redirect_uri=http://localhost:3000/kakaologin&response_type=code&scope=account_email"
      );
    },
  },
};
</script>