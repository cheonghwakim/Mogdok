import Logo from '../components/ui/Logo.vue';

export default {
   title: 'Core/Logo',
   component: Logo,
};

const Template = () => ({
   components: { Logo },
   template: '<logo />',
});

export const Default = Template.bind({});
