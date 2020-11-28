Vue.component('popup-unit-data', {
  props: ['showModal'],
      data() {
          return {
          }
        },
  methods: {
    closeModal: function() {
        this.$emit("close");

    }
  },
  template: `<div v-if="showModal">
                 <div class="modal-mask">
                     <div class="modal-wrapper">
                         <div class="modal-container">
                             <div class="modal-header">
                                 <slot name="modal-header-title"></slot>
                                 <div class="modal-close close" v-on:click="closeModal"></div>
                                 </div>
                             </div>
                             <slot name="modal-body">
                              </slot>
                             <div class="modal-footer">
                                <slot name="modal-footer">
                                 </slot>
                             </div>
                         </div>
                     </div>
                 </div>
             </div>`
});
Vue.component('modal-body-list', {
    props: ['list'],
    data() {
        return {
            countItems: 0
        }
      },
    methods: {
      addItemIntoList: function () {
                        console.log(3);
                this.$emit('item-id', ++this.countItems);
      },
      removeItemOfList: function (index) {
                this.list.items.splice(index, 1);
      }
      },
    template: `<div class="modal-body">
                   <input v-model="list.title" class="input-olive" name="list-name" placeholder="Введите название">
                   <ul class="list-items">
                       <li v-for="(item, index) in list.items" :key="item.it_id" class="list-item">
                           <input v-model="item.is_marked" name="list-item-checkbox" type="checkbox">
                           <input v-model="item.text" name="list-item-text" type="text" placeholder="Введите пункт">
                           <div class="close" v-on:click="removeItemOfList(index)"></div>
                       </li>
                   </ul>
                   <div class="list-items-control">
                       <button name="list-item-add" v-on:click="addItemIntoList">Добавить новый пункт</button>
                   </div>
               </div>`
});
Vue.component('list-item', {
  props: ['item'],
  template: `<li class="item-field-list__items">
                <label :for="item.id"><input v-model="item.is_marked" name="list-item-checkbox" type="checkbox" :id="item.id">{{ item.text }}</label>
             </li>`
});
Vue.component('tag', {
  props: ['tag'],
  template: `<li class="field-lists__list_tag field-unit-tag">
                {{ tag.name }}
             </li>`
});

Vue.component('field-list', {
  props: ['list', 'lists'],
    methods: {
       removeList: function () {
            fetch(`http://127.0.0.1:8080/delete-list/${this.list.id}`
            ,{
              method: "DELETE"
            })
              .then(response => {
                if (!response.ok) { throw Error(response.statusText); }
                else {
                    this.lists.splice(this.lists.indexOf(this.list), 1);
                }
              })
              .catch(error => console.log(error));
       },
       editList: function () {
            this.$emit('edit', this.list);
       }
    },
      template: `<li class="field-lists__list field-list field-unit">
                     <button class="field-list__btn-remove btn-remove-close" v-on:click="removeList">Удалить</button>
                     <div class="field-list__wrapper">
                        <span class="field-list__update-date">{{ list.date_update }}</span>
                        <h4 class="field-list__title">{{ list.title }}</h4>
                        <ul class="field-list__items">
                            <list-item v-for="(item, index) in list.items" :key="item.id" :item="item"/>
                        </ul>
                     </div>
                     <div class="field-lists__list_tags field-unit-tags">
                        <tag v-for="(tag, index) in list.tags" :key="tag.id" :id-tag="tag.id" :item="tag"/>
                     </div>
                     <button class="field-list__btn-edit btn-edit-close" v-on:click="editList">Редактировать</button>
                 </li>`

});
Vue.component('field-lists', {
  props: ['lists'],
  template: `<div class="field-lists">
                  {{ lists }}
                <ul class="field-list">
                    <field-list v-for="(list, index) in lists" :key="list.id" :list="list" :lists="lists" v-on:edit="editList" />
                </ul>
             </div>`,
    created: function() {
        console.log(this.lists);
            fetch("http://127.0.0.1:8080/lists", {
              method: "GET"
            })
              .then(response => {
                if (!response.ok) throw Error(response.statusText);
                return response.json();
              }).then(data => { data.forEach(i => this.lists.push(i))})
              .catch(error => console.log(error));
        },
    methods: {
        editList: function(l){
            this.$emit('edit', l);
        },
      }
  })

let app = new Vue({
  el: '#app',
  data: {
    showPopUpListAdd: false,
    showPopUpListChange: false,
    newList: {
        title: "",
        items: [{
            it_id: 0,
            is_marked: false,
            text: ""
        }]},
    modifiedList: {
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
        addItemIntoNewList: function (it_id) {
                  console.log(it_id);
                  this.newList.items.push({it_id: it_id, is_marked: false, text: ""});
              },
        addItemIntoModifiedList: function (it_id) {
                  console.log(it_id);
                  this.modifiedList.items.push({it_id: it_id, is_marked: false, text: ""});
              },
      pushList: function(){
      console.log(this.newList);
      let n = JSON.stringify(this.newList);
            console.log(n);
          this.showPopUpListAdd = false;
                fetch("http://127.0.0.1:8080/push-list", {
                  method: "POST",
                  headers: {
                    'Content-Type': 'application/json'
                  },
                  body: n
                })
                  .then(response => {
                    if (!response.ok) throw Error(response.statusText);
                    return response.json();
                  })
                  .then(data => {console.log(data); this.lists.push(data);})
                  .catch(error => console.log(error));
                  console.log(this.newList);
                        this.newList = {
                         title: "",
                         items: [{
                             it_id: 0,
                             is_marked: false,
                             text: ""
                         }]};
      },
      changeList: function(){
          this.showPopUpListChange = false;
          fetch("http://127.0.0.1:8080/update", {
            method: "PUT",
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(this.modifiedList)
          })
            .then(response => {
              if (!response.ok) throw Error(response.statusText);
              return response.json();
            })
            .then(data => {
                console.log(data);
                let index = 0;
                this.lists.forEach((e, i) => {
                    if (e.id === data.id) { index = i; }
                });
                this.lists.splice(index, 1, data);
            })
            .catch(error => console.log(error));

      },
      editList: function(editList){
          this.showPopUpListChange = true;
          this.modifiedList = JSON.parse(JSON.stringify(editList));
          console.log(this.modifiedList);
      },
      closePopUpAdd: function(){
          this.showPopUpListAdd = false;
      },
      closePopUpChange: function(){
          this.showPopUpListChange = false;
      }}
})