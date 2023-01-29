import requests
import re
import sys

def classify(symptom, chapters):

    symptom_chapter = set()

    for s_list in chapters:
        for chapter in s_list["name"].split(" "):
            if re.search(chapter, symptom, re.IGNORECASE):
                if chapter == "inner" or chapter == "outer" or chapter == "internal" or chapter == "external":
                    if s_list["name"].split(" ")[0] not in symptom:
                        continue
                symptom_chapter.add(chapter)

    return list(symptom_chapter)
def pipeline(symptom):

    chapters = {}

    symptom_chapter = []
    final_output = []

    chaptersRequest = requests.get('http://dagere.comiles.eu:8090/api/v1/chapters/fetchChapters')
    if chaptersRequest.status_code == 200:
        chapters = chaptersRequest.json()

    symptom_chapter = classify(symptom, chapters)

    final_output.append("".join(symptom_chapter))
    final_output.append("no subchapter found")

    return final_output

'''
if __name__ == "__main__":
    s = pipeline(" ".join(sys.argv[1:]))
    print(s)
'''