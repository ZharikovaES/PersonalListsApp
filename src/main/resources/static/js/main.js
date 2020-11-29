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
Vue.component('modal-body-note', {
    props: ['note'],
    template: `<div class="modal-body">
                   <input v-model="note.title" class="input-olive" name="note-name" type="text" placeholder="Введите название">
                   <textarea v-model="note.text" rows="10" cols="50" name="note-text" placeholder="Введите заметку"></textarea>
               </div>`
});
Vue.component('modal-body-tag', {
    props: ['newTag'],
//    data() {
//        return {
//            colorPicker: (new iro.ColorPicker('#picker')).on('color:change', getColor),
//            newTag: {
//                name: "",
//                color: ""
//            }
//        }
//    },
    methods: {
//        getColor: function(color) {
//            let rgba = color.rgbaString;
//            console.log(rgba);
//            this.newTag.color = rgba;
//            this.$emit"tag", newTag);
//        }
    },
    template: `<div class="modal-body">
                   <input v-model="newTag.name" class="input-olive" name="tag-input" type="text" placeholder="Введите название">
//                   <div id="picker"></div>
               </div>`,
});
Vue.component('list-item', {
  props: ['item'],
  template: `<li class="item-field-list__items">
                <label :for="item.id"><input v-model="item.is_marked" name="list-item-checkbox" type="checkbox" :id="item.id">{{ item.text }}</label>
             </li>`
});
Vue.component('tag', {
  props: ['tag'],
  template: `<li class="field-lists__list_tag field-unit-tag" :style="background-color: tag.color">
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
                     <div class="field-lists__list-tags field-unit-tags">
                        <tag v-for="(tag, index) in list.tags" :key="tag.id" :id-tag="tag.id" :item="tag"/>
                     </div>
                     <button class="field-list__btn-edit btn-edit-close" v-on:click="editList">Редактировать</button>
                 </li>`

});
Vue.component('field-note', {
  props: ['note', 'notes'],
    methods: {
       removeNote: function () {
            fetch(`http://127.0.0.1:8080/delete-note/${this.note.id}`
            ,{
              method: "DELETE"
            })
              .then(response => {
                if (!response.ok) { throw Error(response.statusText); }
                else {
                    this.notes.splice(this.notes.indexOf(this.note), 1);
                }
              })
              .catch(error => console.log(error));
       },
       editNote: function () {
            this.$emit('edit', this.note);
       }
    },
      template: `<li class="field-notes__note field-note field-unit">
                     <button class="field-note__btn-remove btn-remove-close" v-on:click="removeNote">Удалить</button>
                     <div class="field-note__wrapper">
                        <span class="field-note__update-date">{{ note.date_update }}</span>
                        <h4 class="field-note__title">{{ note.title }}</h4>
                        <p class="field-note__text">{{ note.text }}</p>
                     </div>
                     <div class="field-notes__note-tags field-unit-tags">
                        <tag v-for="(tag, index) in note.tags" :key="tag.id" :id-tag="tag.id" :item="tag"/>
                     </div>
                     <button class="field-note__btn-edit btn-edit-close" v-on:click="editNote">Редактировать</button>
                 </li>`

});
Vue.component('field-lists', {
  props: ['lists'],
  template: `<ul class="field-list">
                    <field-list v-for="(list, index) in lists" :key="list.id" :list="list" :lists="lists" v-on:edit="editList" />
             </ul>`,
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
Vue.component('field-notes', {
  props: ['notes'],
  template: `<ul class="field-note">
                <field-note v-for="(note, index) in notes" :key="note.id" :note="note" :notes="notes" v-on:edit="editNote" />
             </ul>`,
    created: function() {
        console.log(this.notes);
            fetch("http://127.0.0.1:8080/notes", {
              method: "GET"
            })
              .then(response => {
                if (!response.ok) throw Error(response.statusText);
                return response.json();
              }).then(data => { data.forEach(i => this.notes.push(i))})
              .catch(error => console.log(error));
        },
    methods: {
        editNote: function(n){
            this.$emit('edit', n);
        },
      }
  })

let app = new Vue({
  el: '#app',
  data: {
    showPopUpListAdd: false,
    showPopUpListChange: false,
    showPopUpNoteAdd: false,
    showPopUpNoteChange: false,
    showPopUpTagAdd: false,
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
    lists: [],
    newNote: {
        title: "",
        text: ""
        },
    modifiedNote: {
        title: "",
        text: ""
        },
    notes: []
    },
    newTag: {
        name: ""
        },
    tags: []
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
          this.showPopUpListAdd = false;
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
                  .then(data => {console.log(data); this.lists.push(data);})
                  .catch(error => console.log(error));
                        this.newList = {
                         title: "",
                         items: [{
                             it_id: 0,
                             is_marked: false,
                             text: ""
                         }]};
      },
      pushNote: function(){
          this.showPopUpNoteAdd = false;
                fetch("http://127.0.0.1:8080/push-note", {
                  method: "POST",
                  headers: {
                    'Content-Type': 'application/json'
                  },
                  body: JSON.stringify(this.newNote)
                })
                  .then(response => {
                    if (!response.ok) throw Error(response.statusText);
                    return response.json();
                  })
                  .then(data => this.notes.push(data))
                  .catch(error => console.log(error));
                        this.newNote = {
                         title: "",
                         text: "",
                         };
      },
      pushTag: function(){
          this.showPopUpTagAdd = false;
                fetch("http://127.0.0.1:8080/push-tag", {
                  method: "POST",
                  headers: {
                    'Content-Type': 'application/json'
                  },
                  body: JSON.stringify(this.newTag)
                })
                  .then(response => {
                    if (!response.ok) throw Error(response.statusText);
                  })
                  .then(data => this.tags.push(data))
                  .catch(error => console.log(error));
                        this.newTag = {
                         name: ""
                         };
      },
      changeList: function(){
          this.showPopUpListChange = false;
          fetch("http://127.0.0.1:8080/update-list", {
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
      changeNote: function(){
          this.showPopUpNoteChange = false;
          fetch("http://127.0.0.1:8080/update-note", {
            method: "PUT",
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(this.modifiedNote)
          })
            .then(response => {
              if (!response.ok) throw Error(response.statusText);
              return response.json();
            })
            .then(data => {
                console.log(data);
                let index = 0;
                this.notes.forEach((e, i) => {
                    if (e.id === data.id) { index = i; }
                });
                this.notes.splice(index, 1, data);
            })
            .catch(error => console.log(error));
      },
      editNote: function(editNote){
          this.showPopUpNoteChange = true;
          this.modifiedNote = JSON.parse(JSON.stringify(editNote));
          console.log(this.modifiedNote);
      },
      addNewTag: function(newTag) {
        this.newTag = newTag;
      }
      closePopUpAdd: function(){
          this.showPopUpListAdd = this.showPopUpNoteAdd = false;
      },
      closePopUpChange: function(){
          this.showPopUpListChange = this.showPopUpNoteChange = false;
      }
  }
})
