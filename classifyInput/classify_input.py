import re
import json
import requests
import sys


def classify(chapters, subchapters, subtitles, symptom):

    main_chapters = set()
    symptom_subtitles = set()
    symptom_subchapters = set()
    chapter_indexes = set()
    different_chapters = True
    #  case 1: chapter/subtitle found
    for s_list in chapters:
        for chapter in s_list["name"].split(" "):
            if re.search(chapter, symptom, re.IGNORECASE):
                if chapter == "inner" or chapter == "outer" or chapter == "internal" or chapter == "external":
                    if s_list["name"].split(" ")[0] not in symptom:
                        continue
                main_chapters.add(chapter)
                chapter_indexes.add(s_list["id"])
    if len(main_chapters) == 0:
        for s_list in subtitles:
            if re.search(s_list["name"], symptom, re.IGNORECASE):
                symptom_subtitles.add(s_list["name"])
                chapter_indexes.add(s_list["chapterId"])
                chapter_names = findChapter(chapter_indexes, chapters)
                if chapter_names:
                    for name in chapter_names:
                        main_chapters.add(name)
    if len(main_chapters) >= 3:
        different_chapters = True
    else:
        for s_list in subchapters:
            if re.search(s_list["name"], symptom, re.IGNORECASE):
                symptom_subchapters.add(s_list["name"])
                chapter_indexes.add(s_list["chapterId"])
                if len(main_chapters) == 0:
                    chapter_names = findChapter(chapter_indexes, chapters)
                    if chapter_names:
                        for name in chapter_names:
                            main_chapters.add(name.strip())

    if len(chapter_indexes) != 0 and len(symptom_subchapters) == 0:
        for s_list in subchapters:
            if s_list["chapterId"] in chapter_indexes:
                if "General" == s_list["name"]:
                    symptom_subchapters.add("General")
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
                names = chapter["name"].split(" ")
                titles.extend(names)
    for t in titles:
        if t == "" or t == " ":
            titles.remove(t)

    return titles


def match_subchapters(subchapters, chapters, subchapters_dict, chapters_dict):
    subchapters_set = list(subchapters)
    chapters_set = list(chapters)
    ids = set()

    for s_list in subchapters_dict:
        for subchapter_set in subchapters_set:
            if re.search(s_list["name"], subchapter_set, re.IGNORECASE):
                ids.add(s_list["chapterId"])
    for c_list in chapters_dict:
        if c_list["id"] in ids:
            names = c_list["name"].split(" ")
            chapters_set.extend(names)

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
    final_output = []

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

    final_output.append(symptom_chapter)
    final_output.append(symptom_subchapter)

    return final_output
