//.storybook/main.js

// 원활한 테스팅을 위해서 docs파일은 제외
// '../src/**/*.stories.mdx',

module.exports = {
   stories: ['../src/**/*.stories.@(js|jsx|ts|tsx)'],
   addons: ['@storybook/addon-links', '@storybook/addon-essentials'],
};

// 미제공 모듈
// '@storybook/addon-knobs',
// '@storybook/addon-storysource',
