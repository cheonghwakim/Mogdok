import Color from './Color.vue';

export default {
   title: 'Core/Color',
   component: Color,
   argTypes: {
      Color: { control: { type: 'select', options: ['black', 'red', 'green'] } },
   },
};

const Template = (args, { argTypes }) => ({
   props: Object.keys(argTypes),
   components: { Color },
   template: '<color :color="colorName" />',
});

export const Black = Template.bind({});
Black.args = {
   colorName: 'black',
};
export const Red = Template.bind({});
Red.args = {
   colorName: 'red',
};
