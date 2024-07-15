// src/types.ts

export interface Title {
    id: number;
    description: string;
    ownerName: string;
  }
  
  export interface TitleWithHistory {
    title: Title;
    ownerNames: string[];
  }
  