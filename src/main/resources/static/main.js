ace.require("ace/ext/language_tools");
let editor=ace.edit(document.querySelector("#editor"));
editor.setTheme("ace/theme/cobalt");
editor.session.setMode("ace/mode/javaa");
editor.setOptions({
       enableBasicAutocompletion: true,
        enableSnippets: true,
        enableLiveAutocompletion: true
});
window.onload=function(){
       
    // Retrive UUID from localstorage or create UUID for this user / session 
    if (!localStorage.username) {
        localStorage.username = crypto.randomUUID();
    }

    // Now set this UUID as our username ("uname")
    document.getElementsByName("uname")[0].value= localStorage.username;

            document.getElementById("wr1").style.display="none";
            document.getElementById("wr").style.display="block";}
var mirror = CodeMirror.fromTextArea(document.getElementById("mirror"), {
        lineNumbers: true,
      });

function keydown(event){if (event.ctrlKey && event.keyCode === 81) {bodych();}}
function bodych(){
        if(document.getElementById("wr1").style.display=="block")
            {document.getElementById("wr1").blur();
            document.getElementById("wr1").style.display="none";
            document.getElementById("wr").style.display="block";mitoace();}
        else if(document.getElementById("wr").style.display=="block")
            {document.getElementById("wr").blur();
            document.getElementById("wr").style.display="none";
            document.getElementById("wr1").style.display="block";acetomi();}   
        };

editor.commands.addCommand({
    name: "...",
    exec: function () {
   //      if (event.ctrlKey && event.altKey &&event.keyCode === 115) {
      if(document.getElementById("wr").style.display!="none")
        {document.getElementById("wr").style.display="none";
        document.getElementById("wr1").style.display="block";}
    
    },
    bindKey: {mac: "cmd-f", win: "ctrl-q",}
})

mirror.on("keyup", function (cm, event) {
    if (event.ctrlKey && event.keyCode === 81) {
        if(document.getElementById("wr1").style.display!="none")
        {document.getElementById("wr1").style.display="none";
        document.getElementById("wr").style.display="block";}    
    }
        if (!cm.state.completionActive && /*Enables keyboard navigation in autocomplete list*/
            event.keyCode != 13) {/*Enter - do not open autocomplete list just after item has been selected in it*/ 
            CodeMirror.commands.autocomplete(cm, null, {completeSingle: false});
        }
    });
mirror.on("blur",function(){mitoace();});
function acetomi(){
mirror.setValue(editor.getValue());
}

editor.on("blur",function () {acetomi();})
function mitoace(){editor.setValue(mirror.getValue());}
var snippetManager = ace.require("ace/snippets").snippetManager;
        var config = ace.require("ace/config");
         ace.config.loadModule("ace/snippets/javaa", function(m) {
        if (m) {
          snippetManager.files[editor.session.$mode.$id] = m;          
          m.snippets = snippetManager.parseSnippetFile(m.snippetText);
          // or do this if you already have them parsed
        snippetManager.register(m.snippets, m.scope);
          }
        });
function save(){
    let m=editor.getValue();
    var a=m.toString();
    var xhr = new XMLHttpRequest();
    /* Code  when the  data completely  was a POST  request
    var a='{"script":"'+m.toString()+'","username":"'+document.getElementsByName("uname")[0].value+'"}';
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "set.php");
    xhr.setRequestHeader("Content-Type", "application/json");    */
    xhr.open("POST", "set.php?username="+document.getElementsByName("uname")[0].value);
    xhr.setRequestHeader("Content-Type", "text/plain");
    xhr.onload = function () {
        console.log(this.response);
        noti("SAVED","N1");
    };
    xhr.send(a);
}
function run(){    
    document.getElementById("view").innerHTML="Compiling.... \n(कार्य प्रगति पर है....) ";
    var xhr = new XMLHttpRequest();
    var a='{"language":"'+document.getElementsByName("lang")[0].value.toString()+'","username":"'+document.getElementsByName("uname")[0].value.toString()+'"}';
    xhr.open("GET", "get.php?username="+document.getElementsByName("uname")[0].value.toString()+"&language="+document.getElementsByName("lang")[0].value.toString());
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onload = function () {
        console.log("Run: "+this.response);

        console.log("Run: "+this.response);
        var result=JSON.parse(this.response);

        // Store Job Id in local storage for polling later
        localStorage.currentJobID = result.jobId;

        console.log("JOB ID recieved:" + localStorage.currentJobID );

        poll(localStorage.currentJobID );
  
        //notiyfy
        noti("COMPILED","N2");
    };console.log(a); 
    xhr.send(a);
}
function noti(data,n) {
  // Get the snackbar DIV
  var x = document.getElementById(n);
  x.innerHTML=data;
  // Add the "show" class to DIV
  x.className = "show";
  // After sometime, remove the show class from DIV
  setTimeout(function(){ x.className = x.className.replace("show", "");x.textContent="";x.padding="0px;"; }, 5000);
}

function poll(jobId){
    
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "result.php?jobId="+jobId, true);
    xhr.onload = function () {

        console.log(this.response);
        
        const response =  JSON.parse(this.response);

        if(response.status === 'COMPLETED'){

            document.getElementById("view").innerHTML= response.stdout;
            //notiyfy
            noti("COMPILED","N2");
            return; 

        } 
        
        if(response.status === 'FAILED') {
            console.log( "Code failed to execute");
            return; 
        }

        console.log('Job still processing');
        setTimeout(() => poll(jobId), 500);

    };
    xhr.send();
}