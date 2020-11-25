
//Vue.component('todo-item', {
//  template: `<li>
//        <input v-model="item.checked" name="list-item-checkbox" type="checkbox">
//        <input v-model="item.text" name="list-item-text" type="text" placeholder="Введите пункт">
//        <div class="close" v-on:click="item.splice(index, 1)"></div>
//      {{ title }}
//      <button v-on:click="$emit(\'remove\')">Удалить</button>
//    </li>`,
//  props: ['title']
//  })
Vue.component('field-list', {
  template: `<li v-for="(item, index) in newList.items" class="item-field-list__items" :id-item="item.id">
                <label :for="item.id"><input v-model="item.is_marked" name="list-item-checkbox" type="checkbox" :id="item.id"></label>
             </li>`,


});

Vue.component('field-list', {
  template: `<li class="field-list__item item-field-list">
                 <button class="field-list__btn-remove btn-remove-close"v-on:click="$emit(\'remove\')">Удалить</button>
                 <div class="item-field-list__wrapper">
                    <span class="item-field-list__update-date">
                    <h4 class="item-field-list__title"></h4>
                    <ul class="item-field-list__items">

                    </ul>
                 <div>
                 <button class="field-list__btn-edit btn-edit-close"v-on:click="$emit(\'remove\')">Редактировать</button>
             </li>`,


});
Vue.component('field-lists', {
  props: ['lists'],
  template: `<div class="field-lists">
                  {{ lists }}
                <ul class="field-list">

                </ul>

             </div>
    </li>`,
    created: function() {
            fetch("http://127.0.0.1:8080/lists", {
              method: "GET"
            })
              .then(response => {
                if (!response.ok) throw Error(response.statusText);
                return response.json();
              }).then(data => { let l = data; this.lists = new Array(); l.forEach(i => this.lists.push(i))})
              .catch(error => console.log(error));
    }
  })


let app = new Vue({
  el: '#app',
  data: {
    showModal: false,
    newList: {
        countItems: 1,
        title: "",
        items: [{
            it_id: 0,
            is_marked: false,
            text: ""
        }]},
    lists: []
    },
  mounted() {
  },
  methods: {
      addItemIntoList: function () {
        this.newList.items.push({it_id: this.newList.countItems++, is_marked: false, text: ""});
      },
      removeItemOfList: function (index) {
        this.newList.items.splice(index, 1);
        this.newList.countItems--;
      },
      pushList: function(){
          this.showModal = false;
                fetch("http://127.0.0.1:8080/push-list", {
                  method: "POST",
                  headers: {
                    'Content-Type': 'application/json'
                  },
                  body: JSON.stringify(this.newList)
                })
                  .then(response => {
                    if (!response.ok) throw Error(response.statusText);
                    return response.json();
                  })
                  .then(data => console.log(data))
                  .catch(error => console.log(error));
          this.newList = {countItems: 1,
                        title: "",
                        items: [{
                            it_id: 0,
                            is_marked: false,
                            text: ""
                          }]};
          }}

})

console.log(1);