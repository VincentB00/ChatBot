from cgitb import handler
import threading
from urllib.parse import urlparse
import http.server
import socketserver
import signal
import sys
import ChatBot
import json

with open('trainV00.json') as file:
    jsonFile = json.load(file)

chatBotBrain = ChatBot.ChatBotBrain
# chatBotBrain.preprocessingData(jsonFile)
chatBotBrain.loadModel(jsonFile)

encoding = "ISO-8859-1"

class NeuralHTTP(http.server.BaseHTTPRequestHandler):
    def do_GET(self):
        self.send_response(404)
        self.send_header("Content-type", "text/html")
        self.end_headers()
    
    def do_POST(self):
        if '/ChatBot/get_answer' in self.path:
            self.send_response(200)
            self.send_header("Content-type", "application/json")
            self.end_headers()

            # query = urlparse(self.path).query
            # query_components = dict(qc.split("=") for qc in query.split("&"))
            # question = query_components["question"]

            content_len = int(self.headers.get('Content-Length'))
            post_body = self.rfile.read(content_len)
            question = post_body.decode(encoding);
            
            result = {
                "question":question,
                "tag":chatBotBrain.predictTag(question),
                "answer":chatBotBrain.predictAnswer(question)
                }

            self.wfile.write(bytes(json.dumps(result), encoding))

        elif self.path == '/ChatBot/load_model':
            self.send_response(201)
            self.send_header("Content-type", "text/html")
            self.end_headers()
            mainThread = threading.Thread(target=chatBotBrain.loadModel, args=(json.loads(jsonFile),))
            mainThread.start()

        elif self.path == '/ChatBot/train':
            self.send_response(201)
            self.send_header("Content-type", "text/html")
            self.end_headers()

            content_len = int(self.headers.get('Content-Length'))
            post_body = self.rfile.read(content_len)
            jsonFile = post_body.decode(encoding);
            # print(json.loads(jsonFile))
            mainThread = threading.Thread(target=chatBotBrain.createAndTrainModel, args=(json.loads(jsonFile),))
            mainThread.start()
        else:
            self.send_response(404)
            self.send_header("Content-type", "text/html")
            self.end_headers()

PORT = 1111;
HOST = "192.168.1.32"

server = http.server.HTTPServer((HOST, PORT), NeuralHTTP)
print("server now running ...")
server.serve_forever()
server.server_close()
print("Server closing")


def sigint_handler(signal, frame):
    server.server_close()
    print("Server closing")
    print ('KeyboardInterrupt is caught')
    sys.exit(0)

signal.signal(signal.SIGINT, sigint_handler)
