(function(window, document, undefined){
    // code that should be taken care of right away

    url = "http://vincentprivate.synology.me:7070/ChatBotDataCollector/rest/intent";
    

    
    function Submit()
    {
        const tag = document.getElementById("TagTextBox").value.toLowerCase();
        const question = document.getElementById("QuestionTextBox").value;
        const answer = document.getElementById("AnswerTextBox").value;
        
        let request = new XMLHttpRequest();

        request.onreadystatechange = function () 
        {
            if (this.readyState === 4) 
            {
                // const respondJson = JSON.parse(this.responseText);
                if (this.status === 201)
                {
                    window.alert("The data have been added\nthank you for your support\nTo keep adding, just need to refill question and answer");
                    document.getElementById("QuestionTextBox").value = "";
                    document.getElementById("AnswerTextBox").value = "";
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

        // jsonPackage = '{' +
        //     '"tag":'+'"' + tag + '",' +
        //     '"pattern":'+'"' + question + '",' +
        //     '"response":'+'"' + answer + '"' +
        // '}';

        jsonPackage = JSON.stringify({ tag: tag, pattern: question, response: answer});

        console.log(jsonPackage);
        
        request.open("POST", url, true);
        request.send(jsonPackage);
    }

    function SeeData()
    {
        const tag = document.getElementById("TagTextBox").value;
        
        window.open(url + "?tag=" + tag, '_blank');
    }
    
    window.onload = init;
    
      function init()
      {
        // the code to be called when the dom has loaded
        // #document has its nodes

        

        document.getElementById("SubmitButton").onclick = Submit;
        document.getElementById("SeeDataButton").onclick = SeeData;
      }
    
    })(window, document, undefined);



