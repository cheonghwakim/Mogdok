// import axios from '@/services/axios';
// import VueCookies from 'vue-cookies';

// const kakaoHeader = {
//     'Authorization': 'da71a523d1cf8c4b6ed454f3c7f0ccb1',
//     'Content-type': 'application/x-www-form-urlencoded;charset=utf-8',
// };

// const getKakaoToken = async (code) => {
//     console.log('loginWithKakao');
//     try {
//         const data = {
//             grant_type: 'authorization_code',
//             client_id: '330782091b88682f45372582aea53aee',
//             redirect_uri: 'http://localhost:8080/auth',
//             code: code,
//         };
//         const queryString = Object.keys(data)
//             .map(k => encodeURIComponent(k) + '=' + encodeURIComponent(data[k]))
//             .join('&');
            
//         console.log('카카오 토큰', queryString);
//         const result = await axios.post('https://kauth.kakao.com/oauth/token', queryString, { headers: kakaoHeader });

//         return result;
//     } catch (e) {
//         return e;
//     }
// };
// const getKakaoUserInfo = async () => {
//     let data = '';
//     await window.Kakao.API.request({
//         url: "/v2/user/me",
//         success: function (response) {
//             data = response;
//         },
//         fail: function (error) {
//             console.log(error);
//         },
//     });
//     console.log('카카오 계정 정보', data);
//     return data;
// }

// const emailService = () => {
//     const emailLogin = async (email, password) => {
//         const data = {
//             email,
//             password,
//         };
//         try {
//             const { result } = (await axios.post('/emailLogin', data)).data;
//             VueCookies.set('access-token', result.access_token, '60s');
//             VueCookies.set('refresh-token', result.refresh_token, '3d');
//             console.log(result);
//             return result;  
//         } catch (e) {
//             return e;
//         }
//     };
//     const test = async () => {
//         try {
//             const data = await axios.get('/testAPI');
//             console.log('API 성공');
//             return data;
//         } catch (e) {
//             console.log('API 실패');
//             return e;
//         }
//     };

//     return {
//         emailLogin,
//         test,
//     };
// };

// const refreshToken = async () => {
//     try {
//         const { result } = (await axios.get('/refreshToken')).data;
//         VueCookies.set('access-token', result.access_token);
//         console.log('Refresh API 성공', result);
//         return result;
//     } catch (e) {
//         console.log(e);
//     }
// }
// export {
//     getKakaoToken,
//     getKakaoUserInfo,
//     emailService,
//     refreshToken,
// };