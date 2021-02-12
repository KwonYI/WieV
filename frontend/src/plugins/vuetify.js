import Vue from 'vue'
import Vuetify from 'vuetify/lib/framework'

Vue.use(Vuetify)

// export default new Vuetify({
   
// })

const vuetify = new Vuetify({

    theme: {
        themes: {
            mytheme: {
            primary: '#304B61',
            secondary: '#304B61',
            accent: '#304B61',
            error: '#304B61',
          },
        },
      },
})

export default vuetify
