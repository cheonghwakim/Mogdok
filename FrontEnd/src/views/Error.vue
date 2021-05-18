<template lang="">
   <div class="error-view">
      <p class="title">ERROR! ! !</p>
      <img src="@/assets/img/mongsil_err.png" alt="" />
      <p class="desc">치명적인 에러를 만났어요...<br />에러를 알려주시면, 해결을 위해 노력할게요</p>
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
