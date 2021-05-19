<template lang="">
  <div class="error-view">
    <p class="title">404 ERROR ! ! !</p>
    <img src="@/assets/img/mongsil_err.png" alt="" />
    <p class="desc">
      페이지를 찾을 수 없어요...<br />요청하신 페이지가 사라졌거나, 잘못된 경로를 입력하셨어요.
    </p>
  </div>
</template>
<script>
export default {};
</script>
<style scoped lang="scss">
.error-view {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);

  text-align: center;

  .title {
    font-weight: 600;
    color: black;
    font-size: 18pt;
    margin-bottom: 20px;
  }

  .desc {
    font-weight: 600;
    color: rgb(126, 126, 126);
    font-size: 10pt;
    line-height: 18px;
    margin-top: 20px;
  }

  img {
    width: 280px;
  }
}
</style>
