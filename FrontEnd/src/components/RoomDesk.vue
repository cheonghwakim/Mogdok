<template lang="">
   <div class="RoomDesk">
      <div class="user">
         <h3>안양불빠따</h3>
      </div>
      <!-- <img src="@/assets/img/desk.svg" /> -->
   </div>
</template>
<script>
export default {};
</script>
<style scoped lang="scss">
$deskWidth: 25vw;
$deskHeight: 100px;

// 150 * 200 사이즈
// 높이는 4/3*너비 로 구함

.RoomDesk {
   width: inherit;
   min-width: inherit;
   height: calc(#{$deskWidth} * 4 / 3);
   min-height: inherit;

   border: 1px solid red;

   display: flex;
   flex-direction: column;
   align-items: center;

   svg {
      width: 400px;
      height: $deskWidth;
   }
}
</style>
