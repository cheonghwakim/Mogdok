<template>
  <div display="inline">
    <ul>
      <li>
        <v-icon v-if="!playflag" large @click="musicplay()">mdi-play</v-icon>
        <v-icon v-else large @click="musicplay()">mdi-stop</v-icon>
      </li>
      <li>
        <v-select
          v-model="select"
          :items="items"
          item-text="state"
          item-value="source"
          return-object
          single-line
        ></v-select>
      </li>
      <li>
        <v-slider v-model="volume" max="100" min="0"></v-slider>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "bgm",
  data() {
    return {
      select: {
        state: "비 오는 소리",
        source:
          "https://commitmentbucket.s3.ap-northeast-2.amazonaws.com/raining_sound.mp3",
      },
      items: [
        {
          state: "비 오는 소리",
          source:
            "https://commitmentbucket.s3.ap-northeast-2.amazonaws.com/raining_sound.mp3",
        },
        {
          state: "장작 타는 소리",
          source:
            "https://commitmentbucket.s3.ap-northeast-2.amazonaws.com/fireplace_sound.mp3",
        },
        { state: "시골 밤 귀뚜라미 소리", source: "https://commitmentbucket.s3.ap-northeast-2.amazonaws.com/cricket_sound.mp3" },
      ],
      audio: "",
      volume: 100,
      playflag: false,
    };
  },
  mounted() {
    this.audio = new Audio(this.select.source);
    this.audio.loop = true; //반복재생
  },
  methods: {
    musicplay() {
      if (!this.playflag) {
        //재생
        this.audio.load();

        this.playflag = true;
        this.audio.play();
      } else {
        //일시정지
        this.playflag = false;
        this.audio.pause();
      }
    },
  },
  watch: {
    select() {
      //다른 곡 선택
      this.audio.src = this.select.source;//파일 교체
      if (this.playflag) {// 원래 재생되고 있었다면 계속 재생
        this.playflag = false;
        this.musicplay();
      }
    },
    volume() {
      //볼륨조절
      this.audio.volume = this.volume / 100; //0~1까지
    },
  },
};
</script>
<style scoped>
ul {
  list-style: none;
  display: inline;
}

li {
  width: 100px;
  margin: 20px 20px;
  float: left;
}
</style>
