<template>
  <div class="klogin">
    <div class="container d-flex justify-content-center align-items-center">
      <div class="card">
        <div class="row">
          <div class="col-md-6">
            <form class="form">
              <h2>회원가입</h2>

              <div class="inputbox mt-3">
                <span>access_token:  </span>
                <input
                  type="text"
                  placeholder="실명을 입력해주세요"
                  class="form-control"
                  v-model="form.access_token"
                  required
                />
              </div>
              <div class="inputbox mt-3">
                <span>이메일:  </span>
                <input
                  type="text"
                  v-text="form.email"
                  v-model="form.email"
                  class="form-control"
                  required
                />
              </div>
              <div class="inputbox mt-3">
                <span>id:  </span>
                <input
                  type="text"
                  v-text="form.id"
                  v-model="form.id"
                  class="form-control"
                  required
                />
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  created() {
    this.create();
  },
  data() {
    return {
      codes: "",
      form: {
        access_token: "",
        email: "",
        id: "",
      },
    };
  },
  methods: {
    create() {
      this.codes = this.$route.query.code;
      this.getToken();
    },
    login() {
      axios.post("http://localhost:8080/login", this.form).then((res) => {
        if (res.data != null) {
          document.cookie = `accessToken=${res.data}`;
          axios.defaults.headers.common["x-access-token"] = res.data;
          
          // console.log(document.cookie);
          // this.$router.push("/");
        }
      });
    },
    getToken() {
      axios
        .get("http://localhost:8080/klogin?authorize_code=" + this.codes)
        .then((res) => {
          this.form.email = res.data.email;
          this.form.access_token = res.data.access_token;
          this.form.id = res.data.id;
          // console.log(this.form.email);

          if (this.form.email == undefined) {
            alert("올바르지 못한 접근입니다.");
            this.$router.push("/");
          } else {
            this.login();
          }
        });
    },
    // onSubmit(event) {
    //   event.preventDefault();
    //   // alert(JSON.stringify(this.form));
    //   axios.post("http://localhost:8080/join", this.form).then((res) => {
    //     console.log(res.status);
    //     this.login();
    //   });
    // },

  },
};
</script>