(function(window, document, undefined){
    // code that should be taken care of right away

    url = "http://vincentprivate.synology.me:7070/ChatBotDataCollector/rest";
    
    function train()
    {
        var password = window.prompt("Password: ");

        let request = new XMLHttpRequest();

        request.onreadystatechange = function () 
        {
            if (this.readyState === 4) 
            {
                if (this.status === 201)
                {
                    console.log("Looking Good");
                }
                else if (this.status === 403)
                {
                    window.alert("Wrong password");
                }
                else if (this.response == null && this.status === 0) 
                {
                    document.body.className = 'error offline';
                    console.log("The computer appears to be offline.");
                } 
                else if(this.status > 201)
                {
                    window.alert("Something when wrong, please try again");
                    console.log(this.responseText)
                }
                else 
                {
                    document.body.className = 'error';
                }
            }
        };
        
        request.open("POST", url + "/train_model", true);
        request.send(password.trim());
    }
    
    function Submit()
    {
        let request = new XMLHttpRequest();

        request.onreadystatechange = function () 
        {
            if (this.readyState === 4) 
            {
                if (this.status === 200)
                {
                    const respondJson = JSON.parse(this.responseText);

                    const tag = document.getElementById("Editbox1");
                    const question = document.getElementById("Editbox2");
                    const answer = document.getElementById("Editbox3");
                    console.log(respondJson);
                    tag.value = respondJson.tag;
                    question.value = respondJson.question;
                    answer.value = respondJson.answer;

                    var textarea = document.getElementById('AnswerTextBox');
                    textarea.value += "\nHUMAN: " + respondJson.question;
                    document.getElementById("AnswerTextBox").value += "\nBOT: " + respondJson.answer;
                    
                    textarea.scrollTop = textarea.scrollHeight;
                }
                else if (this.response == null && this.status === 0) 
                {
                    document.body.className = 'error offline';
                    console.log("The computer appears to be offline.");
                } 
                else if(this.status > 201)
                {
                    window.alert("Something when wrong, please try again");
                    console.log(this.responseText)
                }
                else 
                {
                    document.body.className = 'error';
                }
            }
        };
        
        request.open("POST", url + "/answer", true);

        body = document.getElementById("QuestionTextBox").value;
        document.getElementById("QuestionTextBox").value = "";

        if(body === "")
            return;

        request.send(body);
    }

    window.addEventListener("keydown", function (event) 
    {
        if (event.defaultPrevented) 
        {
            return; // Do nothing if the event was already processed
        }
    
        switch (event.key)
        {
            case "Enter":
                Submit();
                break;
            default:
                return; // Quit when this doesn't handle the key event.
        }
    
        // Cancel the default action to avoid it being handled twice
        event.preventDefault();
    }, true);
    // the last option dispatches the event to the listener first,
    // then dispatches event to window
    
    window.onload = init;
    
      function init()
      {
        // the code to be called when the dom has loaded
        // #document has its nodes

        document.getElementById("SubmitButton").onclick = Submit;
        document.getElementById("TrainButton").onclick = train;

      }
    
    })(window, document, undefined);



