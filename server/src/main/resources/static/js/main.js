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
                                 <h4 class="modal-header-title">
                                    <slot name="modal-header-title"></slot>
                                 </h4>
                                 <div class="modal-close close" v-on:click="closeModal"></div>
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
            countItems: 0,
            countTags: 0
        }
      },
    methods: {
      addItemIntoList: function () {
                this.$emit('item-id', ++this.countItems);
      },
      addTagIntoList: function () {
                this.$emit('tag-id', ++this.countTags);
      },
      removeItemOfList: function (index) {
                this.list.items.splice(index, 1);
      },
      removeTagOfList: function (index) {
                this.list.tags.splice(index, 1);
      }
      },
    template: `<div class="modal-body">
                   <input v-model="list.title" class="input-olive input-dark-green" name="list-name" placeholder="Введите название">
                   <ul class="list-items">
                       <li v-for="(item, index) in list.items" :key="item.it_id" class="list-item">
                           <input v-model="item.is_marked" name="list-item-checkbox" type="checkbox">
                           <input class="input-dark-green" v-model="item.text" name="list-item-text" type="text" placeholder="Введите пункт">
                           <div class="close" v-on:click="removeItemOfList(index)"></div>
                       </li>
                   </ul>
                   <h5 class="modal-title-tags">Теги</h5>
                   <ul class="list-tags">
                       <li v-for="(tag, index) in list.tags" :key="tag.t_id" class="list-tag">
                           <input class="input-dark-green" v-model="tag.name" name="list-tag-name" type="text" placeholder="Введите тег">
                           <div class="close" v-on:click="removeTagOfList(index)"></div>
                       </li>
                   </ul>
                   <div class="popup-control">
                       <button name="list-item-add"  class="btn-dark-green" v-on:click="addItemIntoList">Добавить новый пункт</button>
                       <button name="list-tag-add"  class="btn-dark-green" v-on:click="addTagIntoList">Добавить тег</button>
                   </div>
               </div>`
});
Vue.component('modal-body-note', {
    props: ['note'],
        data() {
            return {
                countTags: 0,
                showImage: false
            }
          },
        methods: {
          addTagIntoNote: function () {
                    this.$emit('tag-id', ++this.countTags);
          },
          removeTagOfNote: function (index) {
                    this.note.tags.splice(index, 1);
          },
          onFileSelected(event){
          console.log(event.target.files[0])
            this.$emit("image", event.target.files[0]);
          },
            removeImage: function () {
               this.$emit('removei', this.note.filename);
               this.showImage = false;
            },
          },
    template: `<div class="modal-body">
                    <div v-if="showImage" class="note__image">
                        <div class="close" v-on:click="removeImage"></div>
                        <img v-if="note.filename" :src="'/images/' + note.filename" />
                    </div>
                    <input v-model="note.title" class="input-dark-green" name="note-name" type="text" placeholder="Введите название">
                    <textarea class="input-dark-green" v-model="note.text" rows="10" name="note-text" placeholder="Введите заметку"></textarea>
                    <h5 class="modal-title-tags">Теги</h5>
                    <ul class="list-tags">
                      <li v-for="(tag, index) in note.tags" :key="tag.t_id" class="note-tag">
                          <input class="input-dark-green" v-model="tag.name" name="list-tag-name" type="text" placeholder="Введите тег">
                          <div class="close" v-on:click="removeTagOfNote(index)"></div>
                      </li>
                    </ul>
                    <div class="popup-control">
                        <button name="note-tag-add" class="btn-dark-green" v-on:click="addTagIntoNote">Добавить тег</button>
                        <input type="file" name="image-input" accept="image/*" @change="onFileSelected" multiple>
                    </div>
               </div>`,
               created: function() {
                if(Boolean(this.note)){
                    if (this.note.filename !== '') this.showImage = true;
                    else this.showImage = false;
                   }
               }
});
Vue.component('list-item', {
  props: ['item'],
  methods: {
    check(){
          fetch("http://127.0.0.1:8080/update-check-list", {
            method: "POST",
            headers: {
            'Content-Type': 'application/json'
            },
            body: JSON.stringify({id: this.item.id, is_marked: this.item.is_marked})
          })
        .then(response => {
          if (!response.ok) throw Error(response.statusText);
        })
        .catch(error => console.log(error));

        console.log(this.item.id, this.item.is_marked);

    }
  },
  template: `<li class="item-field-list__items">
                <label :for="item.id"><input @change="check" v-model="item.is_marked" name="list-item-checkbox" type="checkbox" :id="item.id">{{ item.text }}</label>
             </li>`
});
Vue.component('tag', {
  props: ['tag'],
    data(){
       return {
         tagColor: {
//           background-color: ,
         }
       }
    },
  template: `<li class="field-lists__list-tag field-unit-tag">
                {{ tag.name }}
             </li>`
});
Vue.component('v-select', {
    props: ['options'],
    data() {
        return {
            isOptionsVisible: false,
            selected: {name: "Сначала новые записи", value: 0}
        }
    },
    template: `<div class="v-select">
                <p v-on:click="isOptionsVisible = !isOptionsVisible" class="v-select__title">{{ selected.name }}</p>
                <div v-if="isOptionsVisible" class="v-select__options btn-dark-green">
                    <p v-for="option in options" :key="option.value" v-on:click="selectOptions(option)" >{{ option.name }}</p>
                </div>
             </div>`,
    methods: {
        selectOptions(option) {
            this.isOptionsVisible = false;
            this.selected = option;
            this.$emit("selected-check", option.value);
        },
        hideSelect() {
            this.isOptionsVisible = false;
        }
    },
    mounted() {
        document.addEventListener('click', this.hideSelect.bind(this), true);
    },
    beforeDestroy() {
        document.removeEventListener('click', this.hideSelect);
    }
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
                     <div class="field-list__btn-remove btn-remove-close close" v-on:click="removeList"></div>
                     <div class="field-list__wrapper">
                        <span class="field-list__update-date">{{ list.date_update }}</span>
                        <h4 class="field-list__title">{{ list.title }}</h4>
                        <ul class="field-list__items">
                            <list-item v-for="(item, index) in list.items" :key="item.id" :item="item"/>
                        </ul>
                     </div>
                     <div class="field-lists__list-tags field-unit-tags">
                        <tag v-for="(tag, index) in list.tags" :key="tag.id" :id-tag="tag.id" :tag="tag"/>
                     </div>
                     <button class="field-list__btn-edit btn-edit-close btn-dark-green" v-on:click="editList">Редактировать</button>
                 </li>`
});
Vue.component('field-note', {
  props: ['note', 'notes'],
  data() {
    return {
        showImage: this.note.filename !== ''
    }
  },
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
       created: function() {
           if(Boolean(this.note)){
            if (this.note.filename !== '') this.showImage = true;
            else this.showImage = false;
           }
       },
      template: `<li class="field-notes__note field-note field-unit">
                     <div class="field-note__btn-remove btn-remove-close close" v-on:click="removeNote"></div>
                     <div v-if="showImage" class="note__image">
                         <img v-if="note.filename" :src="'/images/' + note.filename" >
                     </div>
                     <div class="field-note__wrapper">
                        <span class="field-note__update-date">{{ note.date_update }}</span>
                        <h4 class="field-note__title">{{ note.title }}</h4>
                        <p class="field-note__text">{{ note.text }}</p>
                     </div>
                     <div class="field-notes__note-tags field-unit-tags">
                        <tag v-for="(tag, index) in note.tags" :key="tag.id" :id-tag="tag.id" :tag="tag"/>
                     </div>
                     <button class="field-note__btn-edit btn-edit-close btn-dark-green" v-on:click="editNote">Редактировать</button>
                 </li>`
});
Vue.component('field-lists', {
  props: ['lists', 'showAllLists'],
  template: `<ul class="field-list">
                    <field-list v-for="(list, index) in lists" :key="list.id" :list="list" :lists="lists" v-on:edit="editList" />
             </ul>`,
    created: function() {
        if(Boolean(this.showAllLists)){
            this.showAllLists();
        }
    },
    methods: {
        editList: function(l){
            this.$emit('edit', l);
        },
      }
  })
Vue.component('field-tags', {
  props: ['tags'],
  template: `<ul class="field-tags">
                    <li v-on:click="showAll" class="field-tag__inner">Все записи</li>
                    <li v-for="(tag, index) in tags" :key="tag.id_db" class="field-tag">
                        <div class="field-tag__inner">
                            <div class="tag-name" v-on:click="sortByTag(tag.id_db)">{{ tag.name }}</div>
                            <div class="tag-close close" v-on:click="deleteTag(index)"></div>
                        </div>
                    </li>
             </ul>`,
    created: function() {
        fetch("http://127.0.0.1:8080/tags", {
          method: "GET"
        })
          .then(response => {
            if (!response.ok) throw Error(response.statusText);
            return response.json();
          }).then(data => {
              console.log(data, this.tags);
              data.forEach(i => this.tags.push(i));
          })
          .catch(error => console.log(error));
        },
    methods: {
        deleteTag(index){
        this.$emit('del', index);
        },
        sortByTag(id){
        this.$emit('sort-tag-id', id);
        },
        showAll(){
        this.$emit('show-all');
        }
      }
  })
Vue.component('field-notes', {
  props: ['notes', 'showAllNotes'],
  template: `<ul class="field-note">
                <field-note v-for="(note, index) in notes" :key="note.id" :note="note" :notes="notes" v-on:edit="editNote" />
             </ul>`,
    created: function() {
            if(Boolean(this.showAllNotes)){
                this.showAllNotes();
            }
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
//    showPopUpTagAdd: false,
    newList: {
        title: "",
        items: [{
            it_id: 0,
            is_marked: false,
            text: ""
        }],
        tags: [{
            t_id: 0,
            id_db: null,
            name: ""
        }]
        },
    modifiedList: {
        items: [{
            it_id: 0,
            is_marked: false,
            text: ""
        }],
        tags: [{
            t_id: 0,
            id_db: null,
            name: ""
        }]
        },
    lists: [],
    newNote: {
        title: "",
        text: "",
        tags: [{
            t_id: 0,
            id_db: null,
            name: ""
          }],
        filename: "",
        file: null
        },
    modifiedNote: {
        title: "",
        text: "",
        tags: [{
              name: ""
          }],
        hasImage: true,
        filename: "",
        file: null
        },
    notes: [],
    newTag: {
        t_id: 0,
        id_db: null,
        name: ""
        },
    modifiedTag: {
        t_id: 0,
        id_db: null,
        name: ""
    },
    tags: [],
    options: [{
        name: "Сначала новые записи", value: 0
    },{
        name: "Сначала старые записи", value: 1
    }],
    tagValueDefault: -1,
    selectedVal: 0,
    searchText: ""
    },
  mounted() {
  },
    methods: {
        addItemIntoNewList: function (it_id) {
                  this.newList.items.push({it_id: it_id, is_marked: false, text: ""});
              },
        addItemIntoModifiedList: function (it_id) {
                  this.modifiedList.items.push({it_id: it_id, is_marked: false, text: ""});
              },
        addTagIntoNewList: function (t_id) {
                  this.newList.tags.push({t_id: t_id, name: ""});
              },
        addTagIntoModifiedList: function (t_id) {
                  this.modifiedList.tags.push({t_id: t_id, name: ""});
              },
        addTagIntoNewNote: function (t_id) {
                  this.newNote.tags.push({t_id: t_id, name: ""});
              },
        addTagIntoModifiedNote: function (t_id) {
                  this.modifiedNote.tags.push({t_id: t_id, name: ""});
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
                  .then(data => {console.log(data);
                  this.lists.push(data.list);
                  this.tags = data.tags;
                  })
                  .catch(error => console.log(error));
                    this.newList = {
                     title: "",
                     items: [{
                         it_id: 0,
                         is_marked: false,
                         text: ""
                     }],
                     tags: [{
                         t_id: 0,
                         name: ""
                     }]
                     },
                     this.newTag = {
                         t_id: 0,
                         name: ""
                         };
      },
      pushNote: function(){
          this.showPopUpNoteAdd = false;
          console.log(this.newNote.file);
          if (this.newNote.file === undefined || this.newNote.file === null) {
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
              .then(data => {
                      this.notes.push(data.note);
                      this.tags = data.tags;
                      console.log(this.tags);
              })
              .catch(error => console.log(error));
          } else {
            const json = JSON.stringify(this.newNote);
            const blob = new Blob([json], {
              type: 'application/json'
            });
            const data = new FormData();
            data.append("file", this.newNote.file);
            data.append("note", blob);
                  fetch("http://127.0.0.1:8080/push-note-image", {
                    method: "POST",
                    body: data
                  })
                    .then(response => {
                      if (!response.ok) throw Error(response.statusText);
                      return response.json();
                    })
                    .then(data => {
                      this.notes.push(data.note);
                      this.tags = data.tags;
                      console.log(this.tags);
                      })
                    .catch(error => console.log(error));
          }
        this.newNote = {
         title: "",
         text: "",
            tags: [{
               t_id: 0,
               name: ""
            }]
         };
        this.newTag = {
          t_id: 0,
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
                    if (e.id === data.list.id) { index = i; }
                });
                this.lists.splice(index, 1, data.list);
                this.tags = data.tags;
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
          console.log("filename ", this.modifiedNote.file, this.modifiedNote.hasImage);
          if (this.modifiedNote.hasImage && (this.modifiedNote.file === undefined || this.modifiedNote.file === null)) {
              fetch("http://127.0.0.1:8080/update-note-not-image", {
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
                      if (e.id === data.note.id) { index = i; }
                  });
                  this.notes.splice(index, 1, data.note);
                  this.tags = data.tags;
              })
              .catch(error => console.log(error));
          }
          else if (this.modifiedNote.file === undefined || this.modifiedNote.file === null){
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
                        if (e.id === data.note.id) { index = i; }
                    });
                    this.notes.splice(index, 1, data.note);
                    this.tags = data.tags;
                })
                .catch(error => console.log(error));
          } else {
            const json = JSON.stringify(this.modifiedNote);
            const blob = new Blob([json], {
              type: 'application/json'
            });
            const data = new FormData();
            data.append("file", this.modifiedNote.file);
            data.append("note", blob);
              fetch("http://127.0.0.1:8080/update-note-image", {
                method: "PUT",
                body: data
              })
              .then(response => {
                if (!response.ok) throw Error(response.statusText);
                return response.json();
              })
              .then(data => {
                  console.log(data);
                  let index = 0;
                  this.notes.forEach((e, i) => {
                      if (e.id === data.note.id) { index = i; }
                  });
                  this.notes.splice(index, 1, data.note);
                  console.log(this.notes);
                  this.tags = data.tags;
              })
              .catch(error => console.log(error));
          }
      },
      editNote: function(editNote){
          this.showPopUpNoteChange = true;
          this.modifiedNote = JSON.parse(JSON.stringify(editNote));
          this.modifiedNote.hasImage = this.modifiedNote.filename.length > 0;
      },
      addNewTag: function(newTag) {
        this.newTag = newTag;
      },
      deleteTag: function(index){
          fetch(`http://127.0.0.1:8080/delete-tag/${this.tags[index].id_db}`
          ,{
            method: "DELETE"
          })
            .then(response => {
              if (!response.ok) { throw Error(response.statusText); } else {
                    let delTag = this.tags[index];
                    console.log(delTag.id_db);
                    this.tags.splice(this.tags.indexOf(delTag), 1);
                    this.lists.forEach(l => {
                        l.tags.forEach(t => {
                            if (t.id_db === delTag.id_db) { l.tags.splice(l.tags.indexOf(t), 1); console.log("список", t.id_db); }
                        })

                    });
                    this.notes.forEach(n => {
                        let el;
                        n.tags.forEach(t => {
                            if (t.id_db === delTag.id_db) { n.tags.splice(n.tags.indexOf(t), 1); console.log("заметка", t.id_db); }
                        })
                    });
              }
            })
            .catch(error => console.log(error));
      },
      closePopUpAdd: function(){
          this.showPopUpListAdd = this.showPopUpNoteAdd = false;
      },
      closePopUpChange: function(){
          this.showPopUpListChange = this.showPopUpNoteChange = false;
      },
      sortByTag(id){
        this.tagValueDefault = id;
        this.showAllLists();
        this.showAllNotes();
      },
      showAllLists() {
              fetch("http://127.0.0.1:8080/lists", {
                method: "POST",
                headers: {
                'Content-Type': 'application/json'
                },
                body: JSON.stringify({date: this.selectedVal, id: this.tagValueDefault})
              })
            .then(response => {
              if (!response.ok) throw Error(response.statusText);
              return response.json();
            }).then(data => { this.lists.splice(0, this.lists.length); data.forEach(i => this.lists.push(i))})
            .catch(error => console.log(error));
      },
      showAllNotes() {
            console.log(this.selectedVal);
              fetch("http://127.0.0.1:8080/notes", {
                method: "POST",
                headers: {
                'Content-Type': 'application/json'
                },
                body: JSON.stringify({date: this.selectedVal, id: this.tagValueDefault})
              })
            .then(response => {
              if (!response.ok) throw Error(response.statusText);
              return response.json();
            }).then(data => { this.notes.splice(0, this.notes.length); data.forEach(i => this.notes.push(i))})
            .catch(error => console.log(error));
      },
      showAll() {
              this.tagValueDefault = -1;
              this.showAllLists();
              this.showAllNotes();
        },
      optionSelect(optionVal) {
        this.selectedVal = optionVal;
        this.showAllLists();
        this.showAllNotes();
      },
      searchByName() {
          fetch("http://127.0.0.1:8080/search-name/",{
                method: "POST",
                headers: {
                'Content-Type': 'application/json'
                },
                body: JSON.stringify({title: this.searchText, date: this.selectedVal, id: this.tagValueDefault})
          })
            .then(response => {
              if (!response.ok) throw Error(response.statusText);
              return response.json();
            }).then(data => {
                console.log(data);
                this.lists = data.lists;
                this.notes = data.notes;
            })
            .catch(error => console.log(error));
      },
      saveImageNewNote(file) {
        this.newNote.file = file;
      },
      saveImageModifiedNote(file) {
        console.log(file);
        this.modifiedNote.file = file;
        this.modifiedNote.hasImage = true;
      },
      removeImageNewNote(s){
        this.newNote.filename = s;
      },
      removeImageModifiedNote(s){
        this.modifiedNote.hasImage = false;
        console.log(this.modifiedNote.hasImage);
      }
  }
})
