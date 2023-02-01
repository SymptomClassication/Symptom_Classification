import re
import json
import requests
import sys

subchapters_dict = []
chapters_dict = []
chaptersId = set()
subchaptersId = set()
def classify(chapters, subchapters, subtitles, symptom):

    main_chapters = set()
    symptom_subtitles = set()
    symptom_subchapters = set()
    chapter_indexes = set()
    different_chapters = True
    #  case 1: chapter/subtitle found
    for s_list in chapters:
        for chapter in s_list["name"].split(" "):
            if chapter != " " and chapter != "":
                if re.search(chapter, symptom, re.IGNORECASE):
                    if chapter == "inner" or chapter == "outer" or chapter == "internal" or chapter == "external":
                        if s_list["name"].split(" ")[0] not in symptom:
                            continue
                    main_chapters.add(chapter)
                    chapter_indexes.add(s_list["id"])
                    chapters_dict.append({"id": s_list["id"], "name": s_list["name"].strip()})
                    chaptersId.add(s_list["id"])

    for s_list in subtitles:
        if s_list["name"] != " " and s_list["name"] != "":
            if re.search(s_list["name"], symptom, re.IGNORECASE):
                symptom_subtitles.add(s_list["name"])
                chapter_indexes.add(s_list["id"])
                chapter_names = findChapter(chapter_indexes, chapters)
                if chapter_names:
                    for name in chapter_names:
                        main_chapters.add(name)
                        chapters_dict.append({"id": s_list["chapterId"], "name": name})

    if len(main_chapters) >= 3:
        different_chapters = True
    else:
        for s_list in subchapters:
            if s_list["name"] != " " and s_list["name"] != "":
                if re.search(s_list["name"], symptom, re.IGNORECASE):
                    symptom_subchapters.add(s_list["name"])
                    chapter_indexes.add(s_list["chapterId"])
                    subchaptersId.add(s_list["id"])
                    subchapters_dict.append({"id": s_list["chapterId"], "name": s_list["name"]})
                    if len(main_chapters) == 0:
                        chapter_names = findChapter(chapter_indexes, chapters)
                        if chapter_names:
                            for name in chapter_names:
                                main_chapters.add(name.strip())
                                chapters_dict.append({"id": s_list["chapterId"], "name": name})


    if len(chapter_indexes) != 0 and len(symptom_subchapters) == 0:
        for s_list in subchapters:
            if s_list["chapterId"] in chapter_indexes:
                if "General" == s_list["name"]:
                    symptom_subchapters.add("General")
                    subchapters_dict.append({"id": s_list["chapterId"], "name": "General"})
                    subchaptersId.add(s_list["id"])

    if len(main_chapters) == 0:
        main_chapters.add("unknown")

    if len(symptom_subchapters) > 0:
        main_chapters = match_subchapters(symptom_subchapters, main_chapters, subchapters, chapters)


    return list(main_chapters), list(symptom_subchapters)


def findChapter(chapter_index, chapters):
    titles = []
    for index in chapter_index:
        for chapter in chapters:
            if chapter["id"] == index:
                titles.append(chapter["name"].strip())
                chaptersId.add(chapter["id"])
    for t in titles:
        if t == "" or t == " ":
            titles.remove(t)


    return titles


def match_subchapters(subchapters, chapters, subchapters_origdict, chapters_origdict):
    subchapters_set = list(subchapters)
    chapters_set = list(chapters)
    ids = set()

    for s_list in subchapters_origdict:
        for subchapter_set in subchapters_set:
            if re.search(s_list["name"], subchapter_set, re.IGNORECASE) and s_list["name"] != "General":
                ids.add(s_list["chapterId"])

    for c_list in chapters_origdict:
        if c_list["id"] in ids:
            chapters_set.append(c_list["name"].strip())
            chapters_dict.append({"id": c_list["id"], "name": c_list["name"].strip()})
            chaptersId.add(c_list["id"])


    for t in chapters_set:
        if t == "" or t == " ":
            chapters_set.remove(t)
    return set(chapters_set)
def pipeline(symptom):

    chapters = {}
    subchapters = {}
    subtitles = {}

    symptom_chapter = []
    symptom_subchapter = []
    final_output = set()

    chaptersRequest = requests.get('http://dagere.comiles.eu:8090/api/v1/chapters/fetchChapters')
    if chaptersRequest.status_code == 200:
        chapters = chaptersRequest.json()

    subchaptersRequest = requests.get('http://dagere.comiles.eu:8094/api/v1/subchapters/fetchSubchapters')
    if subchaptersRequest.status_code == 200:
        subchapters = subchaptersRequest.json()

    subtitlesRequest = requests.get('http://dagere.comiles.eu:8098/api/v1/subtitles/fetchSubtitles')
    if subtitlesRequest.status_code == 200:
        subtitles = subtitlesRequest.json()

    symptom_chapter, symptom_subchapter = classify(chapters, subchapters, subtitles, symptom)

    if len(symptom_chapter) == 0:
        symptom_chapter.append("unknown")
    if len(symptom_subchapter) == 0:
        symptom_subchapter.append("unknown")

    if len(chapters_dict) != 0:
        for id in chapters_dict:
            chapter_subchapter = []
            for chapterId in subchapters_dict:
                if id["id"] == chapterId["id"]:
                    chapter_subchapter.append(chapterId["name"])
            if len(chapter_subchapter) != 0:
                final_output.add(id["name"] + ", "+ " , ".join(chapter_subchapter))
            else:
                final_output.add(id["name"])
    if len(final_output) == 0:
        final_output.add("unknown")

    if len(chaptersId) == 0:
        chaptersId.add(-1)
    if len(subchaptersId) == 0:
        subchaptersId.add(-1)

    return [" + ".join(list(final_output)), list(chaptersId), list(subchaptersId)]

if __name__ == '__main__':
    classification = pipeline(" ".join(sys.argv[1:]))
    print(classification)

