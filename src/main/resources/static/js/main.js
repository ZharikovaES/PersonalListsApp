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
          let m = {title: this.newList.title, items: this.newList.items};
          console.log(m);
                fetch("http://127.0.0.1:8080/push_list", {
                  method: "POST",
                  headers: {
                    'Content-Type': 'application/json'
                  },
                  body: JSON.stringify(m)
                })
                  .then(response => {
                    if (!response.ok) throw Error(response.statusText);
                    return response.json();
                  })
                  .then(data => console.log(data))
                  .catch(error => console.log(error));
          }}
})

console.log(1);