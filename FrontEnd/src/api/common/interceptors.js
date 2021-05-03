import store from '../../store';

function setInterceptors(instance) {
  instance.interceptors.request.use(
    function(config) {
      config.headers.common['auth-token'] = store.state.user.authToken;
      return config;
    },
    function(error) {
      return Promise.reject(error);
    }
  );

  return instance;
}

export default setInterceptors;
